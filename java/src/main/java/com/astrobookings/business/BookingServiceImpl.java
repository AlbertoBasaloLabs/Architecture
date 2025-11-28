package com.astrobookings.business;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import com.astrobookings.business.domain.Booking;
import com.astrobookings.business.domain.Flight;
import com.astrobookings.business.domain.FlightStatus;
import com.astrobookings.business.domain.Rocket;
import com.astrobookings.business.exceptions.NotFoundException;
import com.astrobookings.business.exceptions.PaymentException;
import com.astrobookings.business.exceptions.ValidationException;
import com.astrobookings.business.models.CreateBookingRequest;
import com.astrobookings.business.ports.out.BookingRepository;
import com.astrobookings.business.ports.out.FlightRepository;
import com.astrobookings.business.ports.out.NotificationService;
import com.astrobookings.business.ports.out.PaymentGateway;
import com.astrobookings.business.ports.out.RocketRepository;

class BookingServiceImpl implements BookingService {
  private final FlightRepository flightRepository;
  private final RocketRepository rocketRepository;
  private final BookingRepository bookingRepository;
  private final PaymentGateway paymentGateway;
  private final NotificationService notificationService;

  public BookingServiceImpl(BookingRepository bookingRepository, FlightRepository flightRepository, RocketRepository rocketRepository, PaymentGateway paymentGateway, NotificationService notificationService) {
    this.bookingRepository = bookingRepository;
    this.flightRepository = flightRepository;
    this.rocketRepository = rocketRepository;
    this.paymentGateway = paymentGateway;
    this.notificationService = notificationService;
  }

  @Override
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

  @Override
  public List<Booking> findAllBookings() {
    return bookingRepository.findAll();
  }
}
