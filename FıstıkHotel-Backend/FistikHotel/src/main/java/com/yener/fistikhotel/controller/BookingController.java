package com.yener.fistikhotel.controller;


import com.yener.fistikhotel.model.Booking;
import com.yener.fistikhotel.model.response.BookingResponse;
import com.yener.fistikhotel.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookingResponses = bookingService.getAllBookings();
        return ResponseEntity.ok(bookingResponses);
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<String> saveBooking(@PathVariable Long roomId, @RequestBody Booking bookingRequest) {
        String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
        return ResponseEntity.ok(confirmationCode);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<BookingResponse> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        BookingResponse bookingResponse = bookingService.findByBookingConfirmationCode(confirmationCode);
        return ResponseEntity.ok(bookingResponse);

    }

    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email) {
        List<BookingResponse> bookingsByUserEmail = bookingService.getBookingsByUserEmail(email);
        return ResponseEntity.ok(bookingsByUserEmail);
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
    }

}
