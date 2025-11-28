package com.astrobookings.business;

public class ServiceFactory {
    private static final BookingService bookingService = new BookingServiceImpl();
    private static final FlightService flightService = new FlightServiceImpl();
    private static final RocketService rocketService = new RocketServiceImpl();
    private static final FlightCancellationService flightCancellationService = new FlightCancellationServiceImpl();
    private static final PaymentGateway paymentGateway = new PaymentGatewayImpl();
    private static final NotificationService notificationService = new NotificationServiceImpl();

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

    public static PaymentGateway getPaymentGateway() {
        return paymentGateway;
    }

    public static NotificationService getNotificationService() {
        return notificationService;
    }
}
