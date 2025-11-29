package com.astrobookings.core.application.ports;

import java.util.List;
import java.util.Optional;

import com.astrobookings.core.domain.models.Rocket;

public interface RocketRepository {
  Rocket save(Rocket rocket);

  Optional<Rocket> findById(String id);

  List<Rocket> findAll();
}
