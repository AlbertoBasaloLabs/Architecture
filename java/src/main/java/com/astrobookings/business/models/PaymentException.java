package com.astrobookings.business.models;

public class PaymentException extends RuntimeException {
  public PaymentException(String message) {
    super(message);
  }
}
