package com.astrobookings.core.domain.models;

public record CreateRocketRequest(String name, Integer capacity, Double speed) {
}
