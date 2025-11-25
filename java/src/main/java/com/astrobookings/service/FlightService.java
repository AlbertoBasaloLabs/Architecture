package com.astrobookings.service;

import com.astrobookings.data.FlightRepository;
import com.astrobookings.data.RocketRepository;
import com.astrobookings.model.Flight;
import com.astrobookings.model.FlightStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

public class FlightService {
    // BAD SMELL: Direct instantiation (no dependency injection)
    private FlightRepository flightRepository = new FlightRepository();
    private RocketRepository rocketRepository = new RocketRepository();

    public List<Flight> getAvailableFlights() {
        return getAvailableFlights(null);
    }

    public List<Flight> getAvailableFlights(String statusStr) {
        // BAD SMELL: Fetching all and filtering in memory
        return flightRepository.findAll().stream()
                .filter(f -> f.getDepartureDate().isAfter(LocalDateTime.now()))
                .filter(f -> {
                    if (statusStr == null) return true;
                    try {
                        FlightStatus status = FlightStatus.valueOf(statusStr.toUpperCase());
                        return f.getStatus() == status;
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    // BAD SMELL: All business validations in service layer (Transaction Script pattern)
    public Flight createFlight(String rocketId, LocalDateTime departureDate, Double basePrice) {
        // Validation: rocketId must exist
        if (rocketId == null || rocketId.trim().isEmpty()) {
            throw new IllegalArgumentException("Rocket id must be provided");
        }
        
        // Validation: rocket must exist in database
        if (rocketRepository.findById(rocketId).isEmpty()) {
            throw new IllegalArgumentException("Rocket with id " + rocketId + " does not exist");
        }

        // Validation: basePrice must be provided
        if (basePrice == null) {
            throw new IllegalArgumentException("Base price must be provided");
        }

        // Validation: basePrice must be positive and not zero
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Base price must be positive");
        }

        // Validation: departureDate must be in the future
        if (departureDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Departure date must be in the future");
        }

        // Validation: departureDate must not be more than 1 year ahead
        LocalDateTime oneYearFromNow = LocalDateTime.now().plusYears(1);
        if (departureDate.isAfter(oneYearFromNow)) {
            throw new IllegalArgumentException("Departure date cannot be more than 1 year ahead");
        }

        // Create flight with auto-generated ID
        String id = UUID.randomUUID().toString();
        Flight flight = new Flight(
            id,
            rocketId,
            departureDate,
            basePrice,
            5, // Hardcoded minPassengers default
            FlightStatus.SCHEDULED
        );
        
        return flightRepository.save(flight);
    }
}
