package com.astrobookings.business;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.astrobookings.business.domain.Flight;
import com.astrobookings.business.domain.FlightStatus;
import com.astrobookings.business.exceptions.NotFoundException;
import com.astrobookings.business.exceptions.ValidationException;
import com.astrobookings.business.models.CreateFlightRequest;
import com.astrobookings.business.ports.out.FlightRepository;
import com.astrobookings.business.ports.out.RocketRepository;

class FlightServiceImpl implements FlightService {
  private final FlightRepository flightRepository;
  private final RocketRepository rocketRepository;

  public FlightServiceImpl(FlightRepository flightRepository, RocketRepository rocketRepository) {
    this.flightRepository = flightRepository;
    this.rocketRepository = rocketRepository;
  }

  @Override
  public List<Flight> getAvailableFlights() {
    return getAvailableFlights(null);
  }

  @Override
  public List<Flight> getAvailableFlights(String statusStr) {
    return flightRepository.findAll().stream()
        .filter(f -> f.getDepartureDate().isAfter(LocalDateTime.now()))
        .filter(f -> {
          if (statusStr == null)
            return true;
          try {
            FlightStatus status = FlightStatus.valueOf(statusStr.toUpperCase());
            return f.getStatus() == status;
          } catch (IllegalArgumentException e) {
            return false;
          }
        })
        .collect(Collectors.toList());
  }

  @Override
  public Flight createFlight(CreateFlightRequest request) {
    String rocketId = request.rocketId();
    String departureDateStr = request.departureDate();
    Double basePrice = request.basePrice();

    // Parse date (structure validation)
    LocalDateTime departureDate;
    try {
      departureDate = LocalDateTime.parse(departureDateStr);
    } catch (Exception e) {
      throw new ValidationException("Invalid departure date format");
    }

    // Business validations
    if (rocketRepository.findById(rocketId).isEmpty()) {
      throw new NotFoundException("Rocket with id " + rocketId + " does not exist");
    }
    if (basePrice <= 0) {
      throw new ValidationException("Base price must be positive");
    }
    if (departureDate.isBefore(LocalDateTime.now())) {
      throw new ValidationException("Departure date must be in the future");
    }
    LocalDateTime oneYearFromNow = LocalDateTime.now().plusYears(1);
    if (departureDate.isAfter(oneYearFromNow)) {
      throw new ValidationException("Departure date cannot be more than 1 year ahead");
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
        INITIAL_STATUS);
    return flightRepository.save(flight);
  }
}
