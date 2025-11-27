package com.astrobookings.service;

import com.astrobookings.data.BookingRepository;
import com.astrobookings.data.FlightRepository;
import com.astrobookings.data.RocketRepository;
import com.astrobookings.model.Booking;
import com.astrobookings.model.Flight;
import com.astrobookings.model.FlightStatus;
import com.astrobookings.model.Rocket;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class BookingService {
    private FlightRepository flightRepository = new FlightRepository();
    private RocketRepository rocketRepository = new RocketRepository();
    private BookingRepository bookingRepository = new BookingRepository();

    public Booking createBooking(String flightId, String passengerName) {
        if (passengerName == null || passengerName.isEmpty()) {
            throw new IllegalArgumentException("Passenger name is required");
        }
        if (flightId == null || flightId.isEmpty()) {
            throw new IllegalArgumentException("Flight id is required");
        }
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        if (flight.getStatus() == FlightStatus.SOLD_OUT || flight.getStatus() == FlightStatus.CANCELLED) {
             throw new RuntimeException("Cannot book this flight. Status is " + flight.getStatus());
        }
        Rocket rocket = rocketRepository.findById(flight.getRocketId())
                .orElseThrow(() -> new RuntimeException("Rocket not found"));

        List<Booking> bookings = bookingRepository.findByFlightId(flightId);
        if (bookings.size() >= rocket.getCapacity()) {
            throw new RuntimeException("Flight is full");
        }
        double price = flight.getBasePrice();
        long daysUntilDeparture = ChronoUnit.DAYS.between(LocalDateTime.now(), flight.getDepartureDate());
        int MONTH_DAYS =30;
        double DISCOUNT = 0.1;  
        if (daysUntilDeparture > MONTH_DAYS) {
            price = price * (1 - DISCOUNT); 
        }

        Booking booking = new Booking(
                UUID.randomUUID().toString(),
                flightId,
                passengerName,
                LocalDateTime.now(),
                price
        );

        bookingRepository.save(booking);

        List<Booking> updatedBookings = bookingRepository.findByFlightId(flightId);
        boolean statusChanged = false;
        if (updatedBookings.size() >= flight.getMinPassengers() && flight.getStatus() == FlightStatus.SCHEDULED) {
            flight.setStatus(FlightStatus.CONFIRMED);
            statusChanged = true;
        }
        if (updatedBookings.size() >= rocket.getCapacity()) {
            flight.setStatus(FlightStatus.SOLD_OUT);
            statusChanged = true;
        }
        if (statusChanged) {
            flightRepository.save(flight);
        }

        return booking;
    }

    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }
}
