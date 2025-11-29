package com.astrobookings.config;

import com.astrobookings.adapters.out.out.ExternalFactory;
import com.astrobookings.adapters.out.out.RepositoryFactory;
import com.astrobookings.core.application.BookingService;
import com.astrobookings.core.application.FlightCancellationService;
import com.astrobookings.core.application.FlightService;
import com.astrobookings.core.application.RocketService;
import com.astrobookings.core.application.ServiceFactory;
import com.astrobookings.core.application.ports.BookingRepository;
import com.astrobookings.core.application.ports.FlightRepository;
import com.astrobookings.core.application.ports.NotificationService;
import com.astrobookings.core.application.ports.PaymentGateway;
import com.astrobookings.core.application.ports.RocketRepository;

/**
 * Application configuration layer.
 * Wires dependencies and provides service instances.
 * This is where dependency injection happens (DrivenAdapterFactory pattern).
 */
public class AppConfig {

  // Repositories (Driven Adapters - Persistence)
  private static final BookingRepository bookingRepository = RepositoryFactory.getBookingRepository();
  private static final FlightRepository flightRepository = RepositoryFactory.getFlightRepository();
  private static final RocketRepository rocketRepository = RepositoryFactory.getRocketRepository();

  // External Services (Driven Adapters - External)
  private static final PaymentGateway paymentGateway = ExternalFactory.getPaymentGateway();
  private static final NotificationService notificationService = ExternalFactory.getNotificationService();

  // Services (Business Layer)
  private static final BookingService bookingService = ServiceFactory.createBookingService(
      bookingRepository, flightRepository, rocketRepository, paymentGateway, notificationService);

  private static final FlightService flightService = ServiceFactory.createFlightService(
      flightRepository, rocketRepository);

  private static final RocketService rocketService = ServiceFactory.createRocketService(
      rocketRepository);

  private static final FlightCancellationService flightCancellationService = ServiceFactory
      .createFlightCancellationService(
          flightRepository, bookingRepository, paymentGateway, notificationService);

  public static BookingService getBookingService() {
    return bookingService;
  }

  public static FlightService getFlightService() {
    return flightService;
  }

  public static RocketService getRocketService() {
    return rocketService;
  }

  public static FlightCancellationService getFlightCancellationService() {
    return flightCancellationService;
  }
}
