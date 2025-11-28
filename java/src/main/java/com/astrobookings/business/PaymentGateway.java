package com.astrobookings.business;

public interface PaymentGateway {
    String processPayment(String bookingId, double amount);
    void processRefund(String transactionId, double amount);
}
