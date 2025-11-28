package com.astrobookings.business;

import java.util.List;

import com.astrobookings.providers.models.Booking;

public interface NotificationService {
    void notifyFlightConfirmed(String flightId, List<Booking> bookings);
    void notifyFlightCancelled(String flightId, List<Booking> bookings);
}
