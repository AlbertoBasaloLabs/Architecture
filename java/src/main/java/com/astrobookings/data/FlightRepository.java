package com.astrobookings.data;

import com.astrobookings.model.Flight;
import com.astrobookings.model.FlightStatus;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FlightRepository {
    private static final Map<String, Flight> db = new HashMap<>();

    static {
        // Dummy data
        Flight f1 = new Flight("f1", "r1", LocalDateTime.now().plusDays(40), 5000.0, 5, FlightStatus.SCHEDULED);
        Flight f2 = new Flight("f2", "r1", LocalDateTime.now().plusDays(10), 6000.0, 5, FlightStatus.SCHEDULED);
        db.put(f1.getId(), f1);
        db.put(f2.getId(), f2);
    }

    public Flight save(Flight flight) {
        db.put(flight.getId(), flight);
        return flight;
    }

    public Optional<Flight> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    public List<Flight> findAll() {
        return List.copyOf(db.values());
    }
}
