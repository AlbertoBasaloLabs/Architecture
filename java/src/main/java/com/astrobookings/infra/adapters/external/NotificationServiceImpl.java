package com.astrobookings.infra.adapters.external;

import java.util.List;

import com.astrobookings.business.domain.Booking;
import com.astrobookings.business.ports.out.NotificationService;

class NotificationServiceImpl implements NotificationService {

  @Override
  public void notifyFlightConfirmed(String flightId, List<Booking> bookings) {
    System.out.println(
        "[NOTIFICATION SERVICE] Flight " + flightId + " CONFIRMED - Notifying " + bookings.size() + " passenger(s)");

    for (Booking booking : bookings) {
      System.out.println("[NOTIFICATION SERVICE] Sending confirmation email to " + booking.getPassengerName() +
          " (Booking: " + booking.getId() + ")");
    }
  }

  @Override
  public void notifyFlightCancelled(String flightId, List<Booking> bookings) {
    System.out.println(
        "[NOTIFICATION SERVICE] Flight " + flightId + " CANCELLED - Notifying " + bookings.size() + " passenger(s)");

    for (Booking booking : bookings) {
      System.out.println("[NOTIFICATION SERVICE] Sending cancellation email to " + booking.getPassengerName() +
          " (Booking: " + booking.getId() + ", Refund: $" + booking.getFinalPrice() + ")");
    }
  }
}
