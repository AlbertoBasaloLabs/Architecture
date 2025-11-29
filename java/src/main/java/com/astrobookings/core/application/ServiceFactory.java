package com.astrobookings.core.application;

import com.astrobookings.core.application.ports.BookingRepository;
import com.astrobookings.core.application.ports.FlightRepository;
import com.astrobookings.core.application.ports.NotificationService;
import com.astrobookings.core.application.ports.PaymentGateway;
import com.astrobookings.core.application.ports.RocketRepository;

public class ServiceFactory {

  public static BookingService createBookingService(BookingRepository bookingRepository,
      FlightRepository flightRepository, RocketRepository rocketRepository, PaymentGateway paymentGateway,
      NotificationService notificationService) {
    return new BookingServiceImpl(bookingRepository, flightRepository, rocketRepository, paymentGateway,
        notificationService);
  }

  public static FlightService createFlightService(FlightRepository flightRepository,
      RocketRepository rocketRepository) {
    return new FlightServiceImpl(flightRepository, rocketRepository);
  }

  public static RocketService createRocketService(RocketRepository rocketRepository) {
    return new RocketServiceImpl(rocketRepository);
  }

  public static FlightCancellationService createFlightCancellationService(FlightRepository flightRepository,
      BookingRepository bookingRepository, PaymentGateway paymentGateway, NotificationService notificationService) {
    return new FlightCancellationServiceImpl(flightRepository, bookingRepository, paymentGateway, notificationService);
  }
}
