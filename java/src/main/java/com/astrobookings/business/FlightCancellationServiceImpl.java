package com.astrobookings.business;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.astrobookings.business.models.Booking;
import com.astrobookings.business.models.Flight;
import com.astrobookings.business.models.FlightStatus;
import com.astrobookings.business.ports.BookingRepository;
import com.astrobookings.business.ports.FlightRepository;
import com.astrobookings.business.ports.NotificationService;
import com.astrobookings.business.ports.PaymentGateway;

class FlightCancellationServiceImpl implements FlightCancellationService {
  private final FlightRepository flightRepository;
  private final BookingRepository bookingRepository;
  private final PaymentGateway paymentGateway;
  private final NotificationService notificationService;

  public FlightCancellationServiceImpl(FlightRepository flightRepository, BookingRepository bookingRepository,
      PaymentGateway paymentGateway, NotificationService notificationService) {
    this.flightRepository = flightRepository;
    this.bookingRepository = bookingRepository;
    this.paymentGateway = paymentGateway;
    this.notificationService = notificationService;
  }

  private static final int CANCELLATION_THRESHOLD_DAYS = 7;

  @Override
  public int cancelFlightsWithInsufficientPassengers() {
    List<Flight> allFlights = flightRepository.findAll();
    int cancelledCount = 0;

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime cancellationThreshold = now.plusDays(CANCELLATION_THRESHOLD_DAYS);

    for (Flight flight : allFlights) {
      if (flight.getStatus() != FlightStatus.SCHEDULED) {
        continue;
      }

      if (flight.getDepartureDate().isAfter(cancellationThreshold)) {
        continue;
      }

      List<Booking> bookings = bookingRepository.findByFlightId(flight.getId());
      if (bookings.size() >= flight.getMinPassengers()) {
        continue;
      }

      System.out.println("[CANCELLATION SERVICE] Cancelling flight " + flight.getId() +
          " - Only " + bookings.size() + "/" + flight.getMinPassengers() +
          " passengers, departing in " +
          ChronoUnit.DAYS.between(now, flight.getDepartureDate()) + " days");

      flight.setStatus(FlightStatus.CANCELLED);
      flightRepository.save(flight);

      for (Booking booking : bookings) {
        try {
          paymentGateway.processRefund(booking.getPaymentTransactionId(), booking.getFinalPrice());
        } catch (Exception e) {
          System.err.println("[CANCELLATION SERVICE] Failed to process refund for booking " +
              booking.getId() + ": " + e.getMessage());
        }
      }

      notificationService.notifyFlightCancelled(flight.getId(), bookings);

      cancelledCount++;
    }

    System.out.println("[CANCELLATION SERVICE] Cancelled " + cancelledCount + " flight(s)");
    return cancelledCount;
  }
}
