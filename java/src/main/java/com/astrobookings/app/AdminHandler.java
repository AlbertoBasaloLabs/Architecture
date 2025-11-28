package com.astrobookings.app;

import com.astrobookings.business.FlightCancellationService;
import com.astrobookings.config.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP Handler for administrative operations.
 * This is an intentionally "dirty" implementation with direct service instantiation.
 */
public class AdminHandler extends BaseHandler implements HttpHandler {
    private FlightCancellationService cancellationService = AppConfig.getFlightCancellationService();
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        if ("POST".equals(method) && path.equals("/admin/cancel-flights")) {
            handleCancelFlights(exchange);
        } else {
            handleError(exchange, 404, "Not found");
        }
    }
    
    private void handleCancelFlights(HttpExchange exchange) throws IOException {
        try {
            int cancelledCount = cancellationService.cancelFlightsWithInsufficientPassengers();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Flight cancellation check completed");
            response.put("cancelledFlights", cancelledCount);
            
            sendJsonResponse(exchange, 200, response);
        } catch (Exception e) {
            e.printStackTrace();
            handleError(exchange, 500, "Error processing cancellations: " + e.getMessage());
        }
    }
}
