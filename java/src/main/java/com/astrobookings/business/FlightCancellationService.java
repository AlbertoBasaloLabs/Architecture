package com.astrobookings.business;

import com.astrobookings.model.Booking;
import com.astrobookings.model.Flight;
import com.astrobookings.model.FlightStatus;
import com.astrobookings.persistence.BookingRepository;
import com.astrobookings.persistence.FlightRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service for handling automatic flight cancellations.
 * This is an intentionally "dirty" implementation with direct dependencies.
 */
public class FlightCancellationService {
    private FlightRepository flightRepository = new FlightRepository();
    private BookingRepository bookingRepository = new BookingRepository();
    private PaymentGateway paymentGateway = new PaymentGateway();
    private NotificationService notificationService = new NotificationService();
    
    private static final int CANCELLATION_THRESHOLD_DAYS = 7;
    
    /**
     * Checks all scheduled flights and cancels those departing within 7 days
     * that haven't reached minimum passenger requirements.
     * Processes refunds and sends notifications for cancelled flights.
     * 
     * @return Number of flights cancelled
     */
    public int cancelFlightsWithInsufficientPassengers() {
        List<Flight> allFlights = flightRepository.findAll();
        int cancelledCount = 0;
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cancellationThreshold = now.plusDays(CANCELLATION_THRESHOLD_DAYS);
        
        for (Flight flight : allFlights) {
            // Only check scheduled flights
            if (flight.getStatus() != FlightStatus.SCHEDULED) {
                continue;
            }
            
            // Check if flight departs within the cancellation threshold
            if (flight.getDepartureDate().isAfter(cancellationThreshold)) {
                continue;
            }
            
            // Check if flight has reached minimum passengers
            List<Booking> bookings = bookingRepository.findByFlightId(flight.getId());
            if (bookings.size() >= flight.getMinPassengers()) {
                continue;
            }
            
            // Cancel the flight
            System.out.println("[CANCELLATION SERVICE] Cancelling flight " + flight.getId() + 
                             " - Only " + bookings.size() + "/" + flight.getMinPassengers() + 
                             " passengers, departing in " + 
                             ChronoUnit.DAYS.between(now, flight.getDepartureDate()) + " days");
            
            flight.setStatus(FlightStatus.CANCELLED);
            flightRepository.save(flight);
            
            // Process refunds for all bookings
            for (Booking booking : bookings) {
                try {
                    paymentGateway.processRefund(booking.getPaymentTransactionId(), booking.getFinalPrice());
                } catch (Exception e) {
                    System.err.println("[CANCELLATION SERVICE] Failed to process refund for booking " + 
                                     booking.getId() + ": " + e.getMessage());
                }
            }
            
            // Send cancellation notifications
            notificationService.notifyFlightCancelled(flight.getId(), bookings);
            
            cancelledCount++;
        }
        
        System.out.println("[CANCELLATION SERVICE] Cancelled " + cancelledCount + " flight(s)");
        return cancelledCount;
    }
}
