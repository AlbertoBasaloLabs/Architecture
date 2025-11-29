package com.astrobookings.core.application;

import java.util.List;

import com.astrobookings.core.domain.models.Booking;
import com.astrobookings.core.domain.models.CreateBookingRequest;

public interface BookingService {
  Booking createBooking(CreateBookingRequest request);

  List<Booking> findAllBookings();
}
