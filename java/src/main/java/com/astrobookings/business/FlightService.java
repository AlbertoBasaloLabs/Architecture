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
    private FlightRepository flightRepository = new FlightRepository();
    private RocketRepository rocketRepository = new RocketRepository();

    public List<Flight> getAvailableFlights() {
        return getAvailableFlights(null);
    }

    public List<Flight> getAvailableFlights(String statusStr) {
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

    public Flight createFlight(String rocketId, LocalDateTime departureDate, Double basePrice) {
        if (rocketId == null || rocketId.trim().isEmpty()) {
            throw new IllegalArgumentException("Rocket id must be provided");
        }
        if (rocketRepository.findById(rocketId).isEmpty()) {
            throw new IllegalArgumentException("Rocket with id " + rocketId + " does not exist");
        }
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Base price must be positive");
        }
        if (departureDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Departure date must be in the future");
        }
        LocalDateTime oneYearFromNow = LocalDateTime.now().plusYears(1);
        if (departureDate.isAfter(oneYearFromNow)) {
            throw new IllegalArgumentException("Departure date cannot be more than 1 year ahead");
        }
        String id = UUID.randomUUID().toString();
        int MIN_PASSENGERS = 5;
        FlightStatus INITIAL_STATUS = FlightStatus.SCHEDULED;
        Flight flight = new Flight(
            id,
            rocketId,
            departureDate,
            basePrice,
            MIN_PASSENGERS,
            INITIAL_STATUS
        );
        return flightRepository.save(flight);
    }
}
