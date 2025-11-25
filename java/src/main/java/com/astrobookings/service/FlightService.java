package com.astrobookings.service;

import com.astrobookings.data.FlightRepository;
import com.astrobookings.model.Flight;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FlightService {
    // BAD SMELL: Direct instantiation
    private FlightRepository flightRepository = new FlightRepository();

    public List<Flight> getAvailableFlights() {
        return getAvailableFlights(null);
    }

    public List<Flight> getAvailableFlights(String statusStr) {
        // BAD SMELL: Fetching all and filtering in memory
        return flightRepository.findAll().stream()
                .filter(f -> f.getDepartureDate().isAfter(LocalDateTime.now()))
                .filter(f -> {
            departureDate,
            basePrice,
            0, 
            com.astrobookings.model.FlightStatus.SCHEDULED
        );
        return flightRepository.save(flight);
    }
}
