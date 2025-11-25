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

public class BookingHandler implements HttpHandler {
    private BookingService bookingService = new BookingService();
    private ObjectMapper objectMapper = new ObjectMapper();

    public BookingHandler() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                Map<String, String> body = objectMapper.readValue(is, Map.class);
                
                String flightId = body.get("flightId");
                String passengerName = body.get("passengerName");

                // BAD SMELL: Validation in controller
                if (passengerName == null || passengerName.isEmpty()) {
                    sendResponse(exchange, 400, "Passenger name is required");
                    return;
                }

                Booking booking = bookingService.createBooking(flightId, passengerName);
                String response = objectMapper.writeValueAsString(booking);

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                sendResponse(exchange, 200, response);

            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "Error: " + e.getMessage());
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
