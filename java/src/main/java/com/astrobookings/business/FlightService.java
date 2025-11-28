package com.astrobookings.business;

import java.util.List;

import com.astrobookings.business.models.CreateFlightRequest;
import com.astrobookings.providers.models.Flight;

public interface FlightService {
    List<Flight> getAvailableFlights();
    List<Flight> getAvailableFlights(String statusStr);
    Flight createFlight(CreateFlightRequest request);
}
