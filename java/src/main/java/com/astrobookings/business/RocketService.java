package com.astrobookings.business;

import java.util.List;
import java.util.UUID;

import com.astrobookings.business.exceptions.ValidationException;
import com.astrobookings.business.models.CreateRocketRequest;
import com.astrobookings.providers.RocketRepository;
import com.astrobookings.providers.models.Rocket;

public class RocketService {
  private RocketRepository rocketRepository = new RocketRepository();

  public List<Rocket> findAllRockets() {
    return rocketRepository.findAll();
  }

  public Rocket createRocket(CreateRocketRequest request) {
    String name = request.name();
    Integer capacity = request.capacity();
    Double speed = request.speed();

    // Business validations only
    if (capacity <= 0) {
      throw new ValidationException("Capacity must be greater than 0");
    }
    if (capacity > 10) {
      throw new ValidationException("Capacity cannot exceed 10 passengers");
    }

    String id = UUID.randomUUID().toString();
    Rocket rocket = new Rocket(id, name, capacity, speed != null ? speed : 0.0);
    return rocketRepository.save(rocket);
  }
}
