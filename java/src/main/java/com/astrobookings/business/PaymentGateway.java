package com.astrobookings.business;

import java.util.UUID;

/**
 * Simulated Payment Gateway for processing payments and refunds.
 * This is an intentionally "dirty" implementation that logs to console
 * instead of making real API calls.
 */
public class PaymentGateway {
    
    private static final double PAYMENT_FAILURE_THRESHOLD = 10000.0;
    
    /**
     * Simulates processing a payment through an external payment gateway.
     * Payments above $10,000 will fail to demonstrate error handling.
     * 
     * @param bookingId The booking identifier
     * @param amount The amount to charge
     * @return Transaction ID if successful
     * @throws RuntimeException if payment fails
     */
    public String processPayment(String bookingId, double amount) {
        System.out.println("[PAYMENT GATEWAY] Processing payment for booking " + bookingId + ": $" + amount);
        
        // Simulate payment failure for high amounts (for demonstration purposes)
        if (amount > PAYMENT_FAILURE_THRESHOLD) {
            System.out.println("[PAYMENT GATEWAY] Payment FAILED - Amount exceeds limit");
            throw new RuntimeException("Payment declined: Amount exceeds maximum allowed");
        }
        
        // Simulate successful payment
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.println("[PAYMENT GATEWAY] Payment successful. Transaction ID: " + transactionId);
        
        return transactionId;
    }
    
    /**
     * Simulates processing a refund through an external payment gateway.
     * 
     * @param transactionId The original transaction ID to refund
     * @param amount The amount to refund
     */
    public void processRefund(String transactionId, double amount) {
        System.out.println("[PAYMENT GATEWAY] Processing refund for transaction " + transactionId + ": $" + amount);
        
        // Simulate refund processing
        try {
            Thread.sleep(100); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("[PAYMENT GATEWAY] Refund successful for transaction " + transactionId);
    }
}
