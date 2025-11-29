package com.astrobookings.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.astrobookings.business.FlightService;
import com.astrobookings.business.models.CreateFlightRequest;
import com.astrobookings.business.models.Flight;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class FlightHandler extends BaseHandler implements HttpHandler {
  private FlightService flightService = AppConfig.getFlightService();

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();

    switch (method) {
      case "GET" -> handleGet(exchange);
      case "POST" -> handlePost(exchange);
      default -> exchange.sendResponseHeaders(405, -1);
    }
  }

  private void handleGet(HttpExchange exchange) throws IOException {
    String query = exchange.getRequestURI().getQuery();
    String status = parseStatusParam(query);

    List<Flight> flights = flightService.getAvailableFlights(status);
    sendJsonResponse(exchange, 200, flights);
  }

  private void handlePost(HttpExchange exchange) throws IOException {
    try {
      InputStream inputStream = exchange.getRequestBody();
      CreateFlightRequest request = objectMapper.readValue(inputStream, CreateFlightRequest.class);
      validateFlightRequest(request);
      Flight flight = flightService.createFlight(request);
      sendJsonResponse(exchange, 201, flight);
    } catch (Exception e) {
      handleBusinessException(exchange, e);
    }
  }

  private void validateFlightRequest(CreateFlightRequest request) {
    if (request.rocketId() == null || request.rocketId().trim().isEmpty()) {
      throw new IllegalArgumentException("Rocket id must be provided");
    }
    if (request.departureDate() == null || request.departureDate().trim().isEmpty()) {
      throw new IllegalArgumentException("Departure date must be provided");
    }
    if (request.basePrice() == null) {
      throw new IllegalArgumentException("Base price must be provided");
    }
  }

  private String parseStatusParam(String query) {
    if (query != null && query.contains("status=")) {
      String[] parts = query.split("status=");
      if (parts.length > 1) {
        return parts[1].split("&")[0];
      }
    }
    return null;
  }
}
