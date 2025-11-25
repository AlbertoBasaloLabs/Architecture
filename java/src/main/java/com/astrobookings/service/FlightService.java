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
                    if (statusStr == null) return true;
                    return f.getStatus().name().equals(statusStr);
                })
                .collect(Collectors.toList());
    }

    public Flight createFlight(String rocketId, LocalDateTime departureDate, double basePrice) {
        // BAD SMELL: Validation in Service (Transaction Script)
        if (departureDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Departure date must be in the future");
        }
        if (departureDate.isAfter(LocalDateTime.now().plusYears(1))) {
            throw new IllegalArgumentException("Departure date cannot be more than 1 year in the future");
        }

        Flight flight = new Flight(
            java.util.UUID.randomUUID().toString(),
            rocketId,
            departureDate,
            basePrice,
            0, 
            com.astrobookings.model.FlightStatus.SCHEDULED
        );
        return flightRepository.save(flight);
    }
}
