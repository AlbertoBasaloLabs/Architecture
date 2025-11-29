package com.astrobookings.providers;

import com.astrobookings.business.ports.BookingRepository;
import com.astrobookings.business.ports.FlightRepository;
import com.astrobookings.business.ports.RocketRepository;

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
