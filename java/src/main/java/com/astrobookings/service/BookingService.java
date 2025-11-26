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
    // BAD SMELL: Direct instantiation of repositories
    private FlightRepository flightRepository = new FlightRepository();
    private RocketRepository rocketRepository = new RocketRepository();
    private BookingRepository bookingRepository = new BookingRepository();

    public Booking createBooking(String flightId, String passengerName) {
        if (passengerName == null || passengerName.isEmpty()) {
            throw new IllegalArgumentException("Passenger name is required");
        }
        // 1. Get Flight
        if (flightId == null || flightId.isEmpty()) {
            throw new IllegalArgumentException("Flight id is required");
        }
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        // Check status
        if (flight.getStatus() == FlightStatus.SOLD_OUT || flight.getStatus() == FlightStatus.CANCELLED) {
             throw new RuntimeException("Cannot book this flight. Status is " + flight.getStatus());
        }

        // 2. Get Rocket
        Rocket rocket = rocketRepository.findById(flight.getRocketId())
                .orElseThrow(() -> new RuntimeException("Rocket not found"));

        // 3. Logic of Capacity
        List<Booking> bookings = bookingRepository.findByFlightId(flightId);
        if (bookings.size() >= rocket.getCapacity()) {
            throw new RuntimeException("Flight is full");
        }

        // 4. Logic of Price
        double price = flight.getBasePrice();
        long daysUntilDeparture = ChronoUnit.DAYS.between(LocalDateTime.now(), flight.getDepartureDate());
        if (daysUntilDeparture > 30) {
            price = price * 0.9; // 10% discount
        }

        // 5. Create Booking
        Booking booking = new Booking(
                UUID.randomUUID().toString(),
                flightId,
                passengerName,
                LocalDateTime.now(),
                price
        );

        // 6. Save Booking
        bookingRepository.save(booking);

        // 7. Side Effect: Check minPassengers and Capacity
        // BAD SMELL: Re-fetching bookings to include the new one
        List<Booking> updatedBookings = bookingRepository.findByFlightId(flightId);
        
        boolean statusChanged = false;

        // Transition to CONFIRMED
        if (updatedBookings.size() >= flight.getMinPassengers() && flight.getStatus() == FlightStatus.SCHEDULED) {
            flight.setStatus(FlightStatus.CONFIRMED);
            statusChanged = true;
        }
        
        // Transition to SOLD_OUT
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
