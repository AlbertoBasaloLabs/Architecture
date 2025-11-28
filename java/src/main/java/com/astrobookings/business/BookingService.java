package com.astrobookings.business;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import com.astrobookings.business.exceptions.NotFoundException;
import com.astrobookings.business.exceptions.PaymentException;
import com.astrobookings.business.exceptions.ValidationException;
import com.astrobookings.business.models.CreateBookingRequest;
import com.astrobookings.providers.BookingRepository;
import com.astrobookings.providers.FlightRepository;
import com.astrobookings.providers.RocketRepository;
import com.astrobookings.providers.models.Booking;
import com.astrobookings.providers.models.Flight;
import com.astrobookings.providers.models.FlightStatus;
import com.astrobookings.providers.models.Rocket;

public class BookingService {
  private FlightRepository flightRepository = new FlightRepository();
  private RocketRepository rocketRepository = new RocketRepository();
  private BookingRepository bookingRepository = new BookingRepository();
  private PaymentGateway paymentGateway = new PaymentGateway();
  private NotificationService notificationService = new NotificationService();

  public Booking createBooking(CreateBookingRequest request) {
    String flightId = request.flightId();
    String passengerName = request.passengerName();

    // Business validations
    Flight flight = flightRepository.findById(flightId)
        .orElseThrow(() -> new NotFoundException("Flight not found"));

    if (flight.getStatus() == FlightStatus.SOLD_OUT || flight.getStatus() == FlightStatus.CANCELLED) {
      throw new ValidationException("Cannot book this flight. Status is " + flight.getStatus());
    }
    Rocket rocket = rocketRepository.findById(flight.getRocketId())
        .orElseThrow(() -> new NotFoundException("Rocket not found"));

    List<Booking> bookings = bookingRepository.findByFlightId(flightId);
    if (bookings.size() >= rocket.getCapacity()) {
      throw new ValidationException("Flight is full");
    }
    double price = flight.getBasePrice();
    long daysUntilDeparture = ChronoUnit.DAYS.between(LocalDateTime.now(), flight.getDepartureDate());
    int MONTH_DAYS = 30;
    double DISCOUNT = 0.1;
    if (daysUntilDeparture > MONTH_DAYS) {
      price = price * (1 - DISCOUNT);
    }

    // Process payment before creating booking
    String bookingId = UUID.randomUUID().toString();
    String transactionId;
    try {
      transactionId = paymentGateway.processPayment(bookingId, price);
    } catch (RuntimeException e) {
      throw new PaymentException("Payment failed: " + e.getMessage());
    }

    Booking booking = new Booking(
        bookingId,
        flightId,
        passengerName,
        LocalDateTime.now(),
        price,
        transactionId);

    bookingRepository.save(booking);

    List<Booking> updatedBookings = bookingRepository.findByFlightId(flightId);
    boolean statusChanged = false;
    boolean flightConfirmed = false;
    if (updatedBookings.size() >= flight.getMinPassengers() && flight.getStatus() == FlightStatus.SCHEDULED) {
      flight.setStatus(FlightStatus.CONFIRMED);
      statusChanged = true;
      flightConfirmed = true;
    }
    if (updatedBookings.size() >= rocket.getCapacity()) {
      flight.setStatus(FlightStatus.SOLD_OUT);
      statusChanged = true;
    }
    if (statusChanged) {
      flightRepository.save(flight);
    }

    // Send notification if flight was just confirmed
    if (flightConfirmed) {
      notificationService.notifyFlightConfirmed(flightId, updatedBookings);
    }

    return booking;
  }

  public List<Booking> findAllBookings() {
    return bookingRepository.findAll();
  }
}

