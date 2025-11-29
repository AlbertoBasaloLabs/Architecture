package com.astrobookings.core.application;

import java.util.List;

import com.astrobookings.core.domain.models.CreateFlightRequest;
import com.astrobookings.core.domain.models.Flight;

public interface FlightService {
  List<Flight> getAvailableFlights();

  List<Flight> getAvailableFlights(String statusStr);

  Flight createFlight(CreateFlightRequest request);
}
