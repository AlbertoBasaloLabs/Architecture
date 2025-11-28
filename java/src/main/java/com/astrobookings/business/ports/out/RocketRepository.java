package com.astrobookings.business.ports.out;

import java.util.List;
import java.util.Optional;

import com.astrobookings.business.domain.Rocket;

public interface RocketRepository {
    Rocket save(Rocket rocket);
    Optional<Rocket> findById(String id);
    List<Rocket> findAll();
}
