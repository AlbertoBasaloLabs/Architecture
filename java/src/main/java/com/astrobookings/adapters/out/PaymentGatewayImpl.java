package com.astrobookings.adapters.out.out;

import java.util.UUID;

import com.astrobookings.core.application.ports.PaymentGateway;

class PaymentGatewayImpl implements PaymentGateway {

  private static final double PAYMENT_FAILURE_THRESHOLD = 10000.0;

  @Override
  public String processPayment(String bookingId, double amount) {
    System.out.println("[PAYMENT GATEWAY] Processing payment for booking " + bookingId + ": $" + amount);

    if (amount > PAYMENT_FAILURE_THRESHOLD) {
      System.out.println("[PAYMENT GATEWAY] Payment FAILED - Amount exceeds limit");
      throw new RuntimeException("Payment declined: Amount exceeds maximum allowed");
    }

    String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    System.out.println("[PAYMENT GATEWAY] Payment successful. Transaction ID: " + transactionId);

    return transactionId;
  }

  @Override
  public void processRefund(String transactionId, double amount) {
    System.out.println("[PAYMENT GATEWAY] Processing refund for transaction " + transactionId + ": $" + amount);

    try {
      Thread.sleep(100); // Simulate network delay
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    System.out.println("[PAYMENT GATEWAY] Refund successful for transaction " + transactionId);
  }
}
