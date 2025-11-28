package com.astrobookings.business;

import java.util.List;

import com.astrobookings.business.domain.Rocket;
import com.astrobookings.business.models.CreateRocketRequest;

public interface RocketService {
    List<Rocket> findAllRockets();
    Rocket createRocket(CreateRocketRequest request);
}
