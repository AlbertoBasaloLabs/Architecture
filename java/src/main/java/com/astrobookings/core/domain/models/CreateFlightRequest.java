package com.astrobookings.core.domain.models;

public record CreateFlightRequest(String rocketId, String departureDate, Double basePrice) {
}
