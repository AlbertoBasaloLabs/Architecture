package com.astrobookings.adapters.in;

import com.astrobookings.config.AppConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.astrobookings.core.application.BookingService;
import com.astrobookings.core.domain.models.Booking;
import com.astrobookings.core.domain.models.CreateBookingRequest;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class BookingHandler extends BaseHandler implements HttpHandler {
  private BookingService bookingService = AppConfig.getBookingService();

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
    Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());

    String flightId = queryParams.get("flightId");
    String passengerName = queryParams.get("passengerName");

    List<Booking> bookings = bookingService.findAllBookings();

    List<Booking> filtered = bookings.stream()
        .filter(b -> flightId == null || b.getFlightId().equals(flightId))
        .filter(b -> passengerName == null || b.getPassengerName().equals(passengerName))
        .collect(Collectors.toList());

    sendJsonResponse(exchange, 200, filtered);
  }

  private void handlePost(HttpExchange exchange) throws IOException {
    try {
      InputStream is = exchange.getRequestBody();
      CreateBookingRequest request = objectMapper.readValue(is, CreateBookingRequest.class);
      validateBookingRequest(request);
      Booking booking = bookingService.createBooking(request);
      sendJsonResponse(exchange, 201, booking);
    } catch (Exception e) {
      handleBusinessException(exchange, e);
    }
  }

  private void validateBookingRequest(CreateBookingRequest request) {
    if (request.flightId() == null || request.flightId().trim().isEmpty()) {
      throw new IllegalArgumentException("Flight id is required");
    }
    if (request.passengerName() == null || request.passengerName().trim().isEmpty()) {
      throw new IllegalArgumentException("Passenger name is required");
    }
  }

}

