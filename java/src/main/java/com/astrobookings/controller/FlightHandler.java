package com.astrobookings.controller;

import com.astrobookings.model.Flight;
import com.astrobookings.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class FlightHandler implements HttpHandler {
    private FlightService flightService = new FlightService();
    private ObjectMapper objectMapper = new ObjectMapper();

    public FlightHandler() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            String status = null;
            // Manual parsing of query params (Inconsistent with other handlers if we had a library)
            if (query != null && query.contains("status=")) {
                String[] parts = query.split("status=");
                if (parts.length > 1) {
                    status = parts[1].split("&")[0];
                }
            }

            List<Flight> flights = flightService.getAvailableFlights(status);
            String response = objectMapper.writeValueAsString(flights);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }
}
