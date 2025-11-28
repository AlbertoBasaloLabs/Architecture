package com.astrobookings.business.ports.out;

public interface PaymentGateway {
    String processPayment(String bookingId, double amount);
    void processRefund(String transactionId, double amount);
}
