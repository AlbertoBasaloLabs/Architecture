package com.astrobookings.business.models;

import java.time.LocalDateTime;

public class Flight {
  private String id;
  private String rocketId;
  private LocalDateTime departureDate;
  private double basePrice;
  private int minPassengers;
  private FlightStatus status;

  public Flight() {
  }

  public Flight(String id, String rocketId, LocalDateTime departureDate, double basePrice, int minPassengers,
      FlightStatus status) {
    this.id = id;
    this.rocketId = rocketId;
    this.departureDate = departureDate;
    this.basePrice = basePrice;
    this.minPassengers = minPassengers;
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRocketId() {
    return rocketId;
  }

  public void setRocketId(String rocketId) {
    this.rocketId = rocketId;
  }

  public LocalDateTime getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDateTime departureDate) {
    this.departureDate = departureDate;
  }

  public double getBasePrice() {
    return basePrice;
  }

  public void setBasePrice(double basePrice) {
    this.basePrice = basePrice;
  }

  public int getMinPassengers() {
    return minPassengers;
  }

  public void setMinPassengers(int minPassengers) {
    this.minPassengers = minPassengers;
  }

  public FlightStatus getStatus() {
    return status;
  }

  public void setStatus(FlightStatus status) {
    this.status = status;
  }
}
