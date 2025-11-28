package com.astrobookings.providers;

import java.util.List;
import java.util.Optional;

import com.astrobookings.providers.models.Booking;

public interface BookingRepository {
    Booking save(Booking booking);
    List<Booking> findAll();
    Optional<Booking> findById(String id);
    List<Booking> findByFlightId(String flightId);
}
