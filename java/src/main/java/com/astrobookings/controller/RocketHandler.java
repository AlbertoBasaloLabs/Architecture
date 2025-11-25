package com.astrobookings.controller;

import com.astrobookings.data.RocketRepository;
import com.astrobookings.model.Rocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class RocketHandler implements HttpHandler {
    // BAD SMELL: Controller accessing Repository directly (No Service layer)
    private RocketRepository rocketRepository = new RocketRepository();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<Rocket> rockets = rocketRepository.findAll();
            String response = objectMapper.writeValueAsString(rockets);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }
}
