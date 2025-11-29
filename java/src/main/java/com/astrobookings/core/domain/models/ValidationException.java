package com.astrobookings.core.domain.models;

public class ValidationException extends RuntimeException {
  public ValidationException(String message) {
    super(message);
  }
}
