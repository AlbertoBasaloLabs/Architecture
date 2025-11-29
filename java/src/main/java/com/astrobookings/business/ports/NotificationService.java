package com.astrobookings.business.ports;

import java.util.List;

import com.astrobookings.business.models.Booking;

public interface NotificationService {
  void notifyFlightConfirmed(String flightId, List<Booking> bookings);

  void notifyFlightCancelled(String flightId, List<Booking> bookings);
}
