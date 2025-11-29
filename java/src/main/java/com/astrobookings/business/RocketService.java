package com.astrobookings.business;

import java.util.List;

import com.astrobookings.business.models.CreateRocketRequest;
import com.astrobookings.business.models.Rocket;

public interface RocketService {
  List<Rocket> findAllRockets();

  Rocket createRocket(CreateRocketRequest request);
}
