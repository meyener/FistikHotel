package com.yener.fistikhotel.service;

import com.yener.fistikhotel.model.Booking;
import com.yener.fistikhotel.model.response.BookingResponse;

import java.util.List;

public interface BookingService {
    List<BookingResponse> getAllBookings();

    String saveBooking(Long roomId, Booking bookingRequest);

    void cancelBooking(Long bookingId);

    BookingResponse findByBookingConfirmationCode(String confirmationCode);

    List<BookingResponse> getBookingsByUserEmail(String email);

    List<Booking> getAllBookingsByRoomId(Long roomId);
}

