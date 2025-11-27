package com.astrobookings.business;

import com.astrobookings.model.Booking;

import java.util.List;

/**
 * Simulated Notification Service for sending emails to passengers.
 * This is an intentionally "dirty" implementation that logs to console
 * instead of sending real emails.
 */
public class NotificationService {
    
    /**
     * Simulates sending confirmation emails to all passengers when a flight is confirmed.
     * 
     * @param flightId The flight identifier
     * @param bookings List of bookings for the flight
     */
    public void notifyFlightConfirmed(String flightId, List<Booking> bookings) {
        System.out.println("[NOTIFICATION SERVICE] Flight " + flightId + " CONFIRMED - Notifying " + bookings.size() + " passenger(s)");
        
        for (Booking booking : bookings) {
            System.out.println("[NOTIFICATION SERVICE] Sending confirmation email to " + booking.getPassengerName() + 
                             " (Booking: " + booking.getId() + ")");
        }
    }
    
    /**
     * Simulates sending cancellation emails to all passengers when a flight is cancelled.
     * 
     * @param flightId The flight identifier
     * @param bookings List of bookings for the flight
     */
    public void notifyFlightCancelled(String flightId, List<Booking> bookings) {
        System.out.println("[NOTIFICATION SERVICE] Flight " + flightId + " CANCELLED - Notifying " + bookings.size() + " passenger(s)");
        
        for (Booking booking : bookings) {
            System.out.println("[NOTIFICATION SERVICE] Sending cancellation email to " + booking.getPassengerName() + 
                             " (Booking: " + booking.getId() + ", Refund: $" + booking.getFinalPrice() + ")");
        }
    }
}
