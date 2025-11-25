package com.astrobookings.controller;

import com.astrobookings.data.RocketRepository;
import com.astrobookings.model.Rocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class RocketHandler extends BaseHandler implements HttpHandler {
    // BAD SMELL: Controller accessing Repository directly (No Service layer)
    private RocketRepository rocketRepository = new RocketRepository();

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
        List<Rocket> rockets = rocketRepository.findAll();
        String response = objectMapper.writeValueAsString(rockets);
        sendJsonResponse(exchange, 200, response);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            Rocket rocket = objectMapper.readValue(is, Rocket.class);
            
            if (rocket.getName() == null || rocket.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Rocket name cannot be empty");
            }
            if (rocket.getCapacity() <= 0) {
                throw new IllegalArgumentException("Rocket capacity must be greater than 0");
            }
            if (rocket.getSpeed() != null && rocket.getSpeed() <= 0) {
                throw new IllegalArgumentException("Rocket speed must be greater than 0");
            }

            if (rocket.getId() == null) {
                rocket.setId(java.util.UUID.randomUUID().toString());
            }
            
            rocketRepository.save(rocket);
            
            String response = objectMapper.writeValueAsString(rocket);
            sendJsonResponse(exchange, 201, response);
        } catch (IllegalArgumentException e) {
            String errorResponse = "{\"error\": \"" + e.getMessage() + "\"}";
            sendJsonResponse(exchange, 400, errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
        }
    }
}
