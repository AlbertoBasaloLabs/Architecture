package com.astrobookings.business.ports;

import java.util.List;
import java.util.Optional;

import com.astrobookings.business.models.Rocket;

public interface RocketRepository {
  Rocket save(Rocket rocket);

  Optional<Rocket> findById(String id);

  List<Rocket> findAll();
}
