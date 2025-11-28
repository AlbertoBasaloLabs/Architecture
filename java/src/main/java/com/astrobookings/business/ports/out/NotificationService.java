package com.astrobookings.business.ports.out;

import java.util.List;

import com.astrobookings.business.domain.Booking;

public interface NotificationService {
    void notifyFlightConfirmed(String flightId, List<Booking> bookings);
    void notifyFlightCancelled(String flightId, List<Booking> bookings);
}
