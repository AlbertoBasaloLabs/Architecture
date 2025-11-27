package com.astrobookings.business;

import com.astrobookings.model.Booking;

import java.util.List;

public class NotificationService {
    
    public void notifyFlightConfirmed(String flightId, List<Booking> bookings) {
        System.out.println("[NOTIFICATION SERVICE] Flight " + flightId + " CONFIRMED - Notifying " + bookings.size() + " passenger(s)");
        
        for (Booking booking : bookings) {
            System.out.println("[NOTIFICATION SERVICE] Sending confirmation email to " + booking.getPassengerName() + 
                             " (Booking: " + booking.getId() + ")");
        }
    }
    
    public void notifyFlightCancelled(String flightId, List<Booking> bookings) {
        System.out.println("[NOTIFICATION SERVICE] Flight " + flightId + " CANCELLED - Notifying " + bookings.size() + " passenger(s)");
        
        for (Booking booking : bookings) {
            System.out.println("[NOTIFICATION SERVICE] Sending cancellation email to " + booking.getPassengerName() + 
                             " (Booking: " + booking.getId() + ", Refund: $" + booking.getFinalPrice() + ")");
        }
    }
}
