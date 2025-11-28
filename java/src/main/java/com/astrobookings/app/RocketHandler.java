package com.astrobookings.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.astrobookings.business.RocketService;
import com.astrobookings.business.ServiceFactory;
import com.astrobookings.business.models.CreateRocketRequest;
import com.astrobookings.providers.models.Rocket;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RocketHandler extends BaseHandler implements HttpHandler {
  private RocketService rocketService = ServiceFactory.getRocketService();

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
    List<Rocket> rockets = rocketService.findAllRockets();
    sendJsonResponse(exchange, 200, rockets);
  }

  private void handlePost(HttpExchange exchange) throws IOException {
    try {
      InputStream is = exchange.getRequestBody();
      CreateRocketRequest request = objectMapper.readValue(is, CreateRocketRequest.class);
      
      // Validate structure at application layer
      validateRocketRequest(request);
      
      // Delegate to business layer
      Rocket rocket = rocketService.createRocket(request);
      
      sendJsonResponse(exchange, 201, rocket);
    } catch (Exception e) {
      handleBusinessException(exchange, e);
    }
  }

  private void validateRocketRequest(CreateRocketRequest request) {
    if (request.name() == null || request.name().trim().isEmpty()) {
      throw new IllegalArgumentException("Rocket name is required");
    }
    if (request.capacity() == null) {
      throw new IllegalArgumentException("Capacity is required");
    }
  }
}
