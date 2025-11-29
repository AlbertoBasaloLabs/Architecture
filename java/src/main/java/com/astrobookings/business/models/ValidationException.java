package com.astrobookings.business.models;

public class ValidationException extends RuntimeException {
  public ValidationException(String message) {
    super(message);
  }
}
