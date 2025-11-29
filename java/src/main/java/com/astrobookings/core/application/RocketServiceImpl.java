package com.astrobookings.core.application;

import java.util.List;
import java.util.UUID;

import com.astrobookings.core.domain.models.CreateRocketRequest;
import com.astrobookings.core.domain.models.Rocket;
import com.astrobookings.core.domain.models.ValidationException;
import com.astrobookings.core.application.ports.RocketRepository;

class RocketServiceImpl implements RocketService {
  private final RocketRepository rocketRepository;

  public RocketServiceImpl(RocketRepository rocketRepository) {
    this.rocketRepository = rocketRepository;
  }

  @Override
  public List<Rocket> findAllRockets() {
    return rocketRepository.findAll();
  }

  @Override
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
