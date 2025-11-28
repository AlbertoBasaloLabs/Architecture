package com.astrobookings.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.astrobookings.business.BookingService;
import com.astrobookings.providers.models.Booking;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class BookingHandler extends BaseHandler implements HttpHandler {
  private BookingService bookingService = new BookingService();

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
      Map<String, String> body = objectMapper.readValue(is, Map.class);

      String flightId = body.get("flightId");
      String passengerName = body.get("passengerName");
      Booking booking = bookingService.createBooking(flightId, passengerName);
      sendJsonResponse(exchange, 201, booking);

    } catch (IllegalArgumentException e) {
      handleError(exchange, 400, e.getMessage());
    } catch (RuntimeException e) {
      if (e.getMessage() != null && e.getMessage().toLowerCase().contains("payment failed")) {
        handleError(exchange, 402, e.getMessage());
      } else if (e.getMessage() != null && e.getMessage().toLowerCase().contains("not found")) {
        handleError(exchange, 404, e.getMessage());
      } else {
        handleError(exchange, 400, e.getMessage());
      }
    } catch (Exception e) {
      e.printStackTrace();
      handleError(exchange, 500, "Internal server error");
    }
  }

}
