package com.astrobookings.providers;

import java.util.List;
import java.util.Optional;

import com.astrobookings.providers.models.Flight;

public interface FlightRepository {
    Flight save(Flight flight);
    Optional<Flight> findById(String id);
    List<Flight> findAll();
}
