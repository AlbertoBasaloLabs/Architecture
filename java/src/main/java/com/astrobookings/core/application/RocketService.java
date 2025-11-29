package com.astrobookings.core.application;

import java.util.List;

import com.astrobookings.core.domain.models.CreateRocketRequest;
import com.astrobookings.core.domain.models.Rocket;

public interface RocketService {
  List<Rocket> findAllRockets();

  Rocket createRocket(CreateRocketRequest request);
}
