package com.astrobookings.business;

import java.util.List;

import com.astrobookings.business.domain.Flight;
import com.astrobookings.business.models.CreateFlightRequest;

public interface FlightService {
    List<Flight> getAvailableFlights();
    List<Flight> getAvailableFlights(String statusStr);
    Flight createFlight(CreateFlightRequest request);
}
