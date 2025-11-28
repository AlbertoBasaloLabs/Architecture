package com.astrobookings.business;

import java.util.List;

import com.astrobookings.business.models.CreateBookingRequest;
import com.astrobookings.providers.models.Booking;

public interface BookingService {
    Booking createBooking(CreateBookingRequest request);
    List<Booking> findAllBookings();
}
