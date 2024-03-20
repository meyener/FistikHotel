package com.yener.fistikhotel.service;

import com.yener.fistikhotel.model.response.BookingResponse;

import java.util.List;

public interface BookingService {
    List<BookingResponse> getAllBookedRooms();
}
