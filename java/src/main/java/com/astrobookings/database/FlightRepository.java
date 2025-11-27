package com.astrobookings.database;

import com.astrobookings.models.Flight;
import com.astrobookings.models.FlightStatus;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FlightRepository {
    private static final Map<String, Flight> db = new HashMap<>();

    static {
        // Dummy data
        var dummyRocketId = "00000000-0000-0000-0000-000000000001";
        var dummyFlight1Id = "10000000-0000-0000-0000-000000000001";
        Flight f1 = new Flight(dummyFlight1Id, dummyRocketId, LocalDateTime.now().plusDays(40), 5000.0, 5, FlightStatus.SCHEDULED);
        var dummyFlight2Id = "10000000-0000-0000-0000-000000000002";
        Flight f2 = new Flight(dummyFlight2Id, dummyRocketId, LocalDateTime.now().plusDays(10), 6000.0, 5, FlightStatus.SCHEDULED);
        var dummyFlight3Id = "10000000-0000-0000-0000-000000000003";
        Flight f3 = new Flight(dummyFlight3Id, dummyRocketId, LocalDateTime.now().plusDays(10), 6000.0, 5, FlightStatus.CANCELLED);
        db.put(f1.getId(), f1);
        db.put(f2.getId(), f2);
        db.put(f3.getId(), f3);
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
