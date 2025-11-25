package com.astrobookings.controller;

import com.astrobookings.model.Booking;
import com.astrobookings.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

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
        // BAD SMELL: Logic inside Controller (Smart Controller)
        Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
        
        String flightId = queryParams.get("flightId");
        String passengerName = queryParams.get("passengerName");

        // BAD SMELL: Controller accessing repository directly to filter!
        java.util.List<Booking> bookings = bookingService.findAllBookings();
        
        java.util.List<Booking> filtered = bookings.stream()
            .filter(b -> flightId == null || b.getFlightId().equals(flightId))
            .filter(b -> passengerName == null || b.getPassengerName().equals(passengerName))
            .collect(java.util.stream.Collectors.toList());

        sendJsonResponse(exchange, 200, filtered);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            Map<String, String> body = objectMapper.readValue(is, Map.class);
            
            String flightId = body.get("flightId");
            String passengerName = body.get("passengerName");

            // BAD SMELL: Validation in controller
            validateBookingInput(flightId, passengerName);

            Booking booking = bookingService.createBooking(flightId, passengerName);
            sendJsonResponse(exchange, 201, booking);

        } catch (IllegalArgumentException e) {
            // BAD SMELL: Catching domain exceptions in controller
            handleError(exchange, 400, e.getMessage());
        } catch (RuntimeException e) {
            // BAD SMELL: Using RuntimeException for business logic errors
            handleError(exchange, 400, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            handleError(exchange, 500, "Internal server error");
        }
    }

    private void validateBookingInput(String flightId, String passengerName) {
        if (flightId == null || flightId.isEmpty()) {
            throw new IllegalArgumentException("Flight id is required");
        }
        
        if (passengerName == null || passengerName.isEmpty()) {
            throw new IllegalArgumentException("Passenger name is required");
        }
    }
}
