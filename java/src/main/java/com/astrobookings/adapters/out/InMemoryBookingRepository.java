package com.astrobookings.adapters.out.out;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.astrobookings.core.domain.models.Booking;
import com.astrobookings.core.application.ports.BookingRepository;

class InMemoryBookingRepository implements BookingRepository {
  private static final Map<String, Booking> db = new HashMap<>();

  @Override
  public Booking save(Booking booking) {
    db.put(booking.getId(), booking);
    return booking;
  }

  @Override
  public Optional<Booking> findById(String id) {
    return Optional.ofNullable(db.get(id));
  }

  @Override
  public List<Booking> findAll() {
    return List.copyOf(db.values());
  }

  @Override
  public List<Booking> findByFlightId(String flightId) {
    return db.values().stream()
        .filter(b -> b.getFlightId().equals(flightId))
        .collect(Collectors.toList());
  }
}
