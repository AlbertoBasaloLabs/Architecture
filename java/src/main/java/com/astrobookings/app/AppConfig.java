package com.astrobookings.app;

import com.astrobookings.adapters.ExternalFactory;
import com.astrobookings.adapters.RepositoryFactory;
import com.astrobookings.business.BookingService;
import com.astrobookings.business.FlightCancellationService;
import com.astrobookings.business.FlightService;
import com.astrobookings.business.RocketService;
import com.astrobookings.business.ServiceFactory;
import com.astrobookings.business.ports.BookingRepository;
import com.astrobookings.business.ports.FlightRepository;
import com.astrobookings.business.ports.NotificationService;
import com.astrobookings.business.ports.PaymentGateway;
import com.astrobookings.business.ports.RocketRepository;

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
