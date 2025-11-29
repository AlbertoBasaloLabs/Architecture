package com.astrobookings.app;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;

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
   * 
   * @param exchange    The HTTP exchange to send the response to.
   * @param statusCode  The status code to send.
   * @param jsonContent The JSON content to send.
   * @throws IOException If an I/O error occurs.
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
   * 
   * @param exchange   The HTTP exchange to send the response to.
   * @param statusCode The status code to send.
   * @param object     The object to serialize and send as JSON.
   * @throws IOException If an I/O error occurs.
   */
  protected void sendJsonResponse(HttpExchange exchange, int statusCode, Object object) throws IOException {
    String jsonContent = objectMapper.writeValueAsString(object);
    sendJsonResponse(exchange, statusCode, jsonContent);
  }

  /**
   * Send an error response with the given status code and message.
   * 
   * @param exchange   The HTTP exchange to send the response to.
   * @param statusCode The status code to send.
   * @param message    The error message to send.
   * @throws IOException If an I/O error occurs.
   */
  protected void handleError(HttpExchange exchange, int statusCode, String message) throws IOException {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", message);
    sendJsonResponse(exchange, statusCode, errorResponse);
  }

  /**
   * Handle business exceptions and send appropriate HTTP responses.
   * 
   * @param exchange The HTTP exchange to send the response to.
   * @param e        The exception to handle.
   * @throws IOException If an I/O error occurs.
   */
  protected void handleBusinessException(HttpExchange exchange, Exception e) throws IOException {
    if (e instanceof com.astrobookings.business.models.ValidationException) {
      handleError(exchange, 400, e.getMessage());
    } else if (e instanceof com.astrobookings.business.models.NotFoundException) {
      handleError(exchange, 404, e.getMessage());
    } else if (e instanceof com.astrobookings.business.models.PaymentException) {
      handleError(exchange, 402, e.getMessage());
    } else if (e instanceof IllegalArgumentException) {
      // DTOs throw IllegalArgumentException for structure validation
      handleError(exchange, 400, e.getMessage());
    } else {
      e.printStackTrace();
      handleError(exchange, 500, "Internal server error");
    }
  }

  /**
   * Send a plain text response (for simple responses without JSON).
   * 
   * @param exchange   The HTTP exchange to send the response to.
   * @param statusCode The status code to send.
   * @param content    The content to send as plain text.
   * @throws IOException If an I/O error occurs.
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
   * 
   * @param query The query string to parse.
   * @return A map of parameter names to values.
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
   * 
   * @param exchange  The HTTP exchange to get the query parameter from.
   * @param paramName The name of the query parameter to get.
   * @return The value of the query parameter, or null if not present.
   */
  protected String getQueryParam(HttpExchange exchange, String paramName) {
    String query = exchange.getRequestURI().getQuery();
    Map<String, String> params = parseQueryParams(query);
    return params.get(paramName);
  }
}
