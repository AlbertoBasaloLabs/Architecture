package com.astrobookings.infra.adapters.persistence;

import com.astrobookings.business.ports.out.BookingRepository;
import com.astrobookings.business.ports.out.FlightRepository;
import com.astrobookings.business.ports.out.RocketRepository;

public class RepositoryFactory {
    private static final BookingRepository bookingRepository = new InMemoryBookingRepository();
    private static final FlightRepository flightRepository = new InMemoryFlightRepository();
    private static final RocketRepository rocketRepository = new InMemoryRocketRepository();

    public static BookingRepository getBookingRepository() {
        return bookingRepository;
    }

    public static FlightRepository getFlightRepository() {
        return flightRepository;
    }

    public static RocketRepository getRocketRepository() {
        return rocketRepository;
    }
}
