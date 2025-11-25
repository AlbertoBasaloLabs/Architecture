package com.astrobookings.controller;

import com.astrobookings.model.Flight;
import com.astrobookings.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class FlightHandler extends BaseHandler implements HttpHandler {
    private FlightService flightService = new FlightService();

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
            InputStream is = exchange.getRequestBody();
            Map<String, Object> body = objectMapper.readValue(is, Map.class);
            
            String rocketId = (String) body.get("rocketId");
            String dateStr = (String) body.get("departureDate");
            Object basePriceObj = body.get("basePrice");
            
            validateFlightInput(rocketId, dateStr, basePriceObj);
            
            double basePrice = ((Number) basePriceObj).doubleValue();
            LocalDateTime departureDate = parseDate(dateStr);
            
            Flight flight = flightService.createFlight(rocketId, departureDate, basePrice);
            
            sendJsonResponse(exchange, 201, flight);
        } catch (IllegalArgumentException e) {
            handleError(exchange, 400, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private String parseStatusParam(String query) {
        // Manual parsing of query params (Inconsistent with other handlers if we had a library)
        if (query != null && query.contains("status=")) {
            String[] parts = query.split("status=");
            if (parts.length > 1) {
                return parts[1].split("&")[0];
            }
        }
        return null;
    }

    private void validateFlightInput(String rocketId, String dateStr, Object basePriceObj) {
        if (rocketId == null || rocketId.trim().isEmpty()) {
            throw new IllegalArgumentException("Rocket id must be provided");
        }
        
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Departure date must be provided");
        }
        
        if (basePriceObj == null) {
            throw new IllegalArgumentException("Base price must be provided");
        }
    }

    private LocalDateTime parseDate(String dateStr) {
        try {
            return LocalDateTime.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid departure date format");
        }
    }
}
