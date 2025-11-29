package com.astrobookings.business;

import java.util.List;

import com.astrobookings.business.models.Booking;
import com.astrobookings.business.models.CreateBookingRequest;

public interface BookingService {
  Booking createBooking(CreateBookingRequest request);

  List<Booking> findAllBookings();
}
