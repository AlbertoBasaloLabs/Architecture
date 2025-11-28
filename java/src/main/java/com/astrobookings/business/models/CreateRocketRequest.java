package com.astrobookings.business.models;

public record CreateRocketRequest(String name, Integer capacity, Double speed) {
}
