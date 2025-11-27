package com.astrobookings.models;

import java.time.LocalDateTime;

public class Booking {
    private String id;
    private String flightId;
    private String passengerName;
    private LocalDateTime bookingDate;
    private double finalPrice;
    private String paymentTransactionId;

    public Booking() {}

    public Booking(String id, String flightId, String passengerName, LocalDateTime bookingDate, double finalPrice, String paymentTransactionId) {
        this.id = id;
        this.flightId = flightId;
        this.passengerName = passengerName;
        this.bookingDate = bookingDate;
        this.finalPrice = finalPrice;
        this.paymentTransactionId = paymentTransactionId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFlightId() { return flightId; }
    public void setFlightId(String flightId) { this.flightId = flightId; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(double finalPrice) { this.finalPrice = finalPrice; }

    public String getPaymentTransactionId() { return paymentTransactionId; }
    public void setPaymentTransactionId(String paymentTransactionId) { this.paymentTransactionId = paymentTransactionId; }
}
