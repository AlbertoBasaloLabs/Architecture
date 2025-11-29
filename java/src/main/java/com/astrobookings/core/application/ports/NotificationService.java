package com.astrobookings.core.application.ports;

import java.util.List;

import com.astrobookings.core.domain.models.Booking;

public interface NotificationService {
  void notifyFlightConfirmed(String flightId, List<Booking> bookings);

  void notifyFlightCancelled(String flightId, List<Booking> bookings);
}
