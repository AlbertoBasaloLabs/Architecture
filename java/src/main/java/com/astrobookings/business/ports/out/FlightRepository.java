package com.astrobookings.business.ports.out;

import java.util.List;
import java.util.Optional;

import com.astrobookings.business.domain.Flight;

public interface FlightRepository {
    Flight save(Flight flight);
    Optional<Flight> findById(String id);
    List<Flight> findAll();
}
