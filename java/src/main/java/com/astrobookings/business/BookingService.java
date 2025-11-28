package com.astrobookings.business;

import java.util.List;

import com.astrobookings.business.domain.Booking;
import com.astrobookings.business.models.CreateBookingRequest;

public interface BookingService {
    Booking createBooking(CreateBookingRequest request);
    List<Booking> findAllBookings();
}
