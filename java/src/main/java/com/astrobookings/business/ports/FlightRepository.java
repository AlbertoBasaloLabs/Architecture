package com.astrobookings.business.ports;

import java.util.List;
import java.util.Optional;

import com.astrobookings.business.models.Flight;

public interface FlightRepository {
  Flight save(Flight flight);

  Optional<Flight> findById(String id);

  List<Flight> findAll();
}
