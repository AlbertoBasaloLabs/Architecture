package com.astrobookings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Base handler with common utilities for all HTTP handlers.
 */
public abstract class BaseHandler {
    protected final ObjectMapper objectMapper;

    public BaseHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Send a JSON response with the given status code and content.
     */
    protected void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonContent) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonContent.length());
        OutputStream os = exchange.getResponseBody();
        os.write(jsonContent.getBytes());
        os.close();
    }

    /**
     * Send a JSON response with an object that will be serialized.
     */
    protected void sendJsonResponse(HttpExchange exchange, int statusCode, Object object) throws IOException {
        String jsonContent = objectMapper.writeValueAsString(object);
        sendJsonResponse(exchange, statusCode, jsonContent);
    }

    /**
     * Send an error response with the given status code and message.
     */
    protected void handleError(HttpExchange exchange, int statusCode, String message) throws IOException {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        sendJsonResponse(exchange, statusCode, errorResponse);
    }

    /**
     * Send a plain text response (for simple responses without JSON).
     */
    protected void sendTextResponse(HttpExchange exchange, int statusCode, String content) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(statusCode, content.length());
        OutputStream os = exchange.getResponseBody();
        os.write(content.getBytes());
        os.close();
    }

    /**
     * Parse query parameters from a query string.
     * Returns a map of parameter names to values.
     */
    protected Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        
        if (query == null || query.isEmpty()) {
            return params;
        }

        for (String pair : query.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                params.put(kv[0], kv[1]);
            } else if (kv.length == 1) {
                params.put(kv[0], "");
            }
        }
        
        return params;
    }

    /**
     * Get a query parameter value, or return null if not present.
     */
    protected String getQueryParam(HttpExchange exchange, String paramName) {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = parseQueryParams(query);
        return params.get(paramName);
    }
}
