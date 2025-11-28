package com.astrobookings.business.models;

public record CreateFlightRequest(String rocketId, String departureDate, Double basePrice) {
}
