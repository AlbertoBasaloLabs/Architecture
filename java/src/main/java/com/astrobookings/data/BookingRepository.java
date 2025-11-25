package com.astrobookings.data;

import com.astrobookings.model.Booking;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookingRepository {
    private static final Map<String, Booking> db = new HashMap<>();

    public Booking save(Booking booking) {
        db.put(booking.getId(), booking);
        return booking;
    }

    public Optional<Booking> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    public List<Booking> findAll() {
        return List.copyOf(db.values());
    }

    public List<Booking> findByFlightId(String flightId) {
        return db.values().stream()
                .filter(b -> b.getFlightId().equals(flightId))
                .collect(Collectors.toList());
    }
}
