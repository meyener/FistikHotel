package com.yener.fistikhotel.controller;


import com.yener.fistikhotel.model.response.BookingResponse;
import com.yener.fistikhotel.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookedRoomController {

    private final BookingService bookingService;

    @GetMapping()
    public ResponseEntity<List<BookingResponse>> getAllBookedRooms(){
        List<BookingResponse> bookingResponses= bookingService.getAllBookedRooms();
        return ResponseEntity.ok(bookingResponses);
    }
}
