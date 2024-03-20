package com.yener.fistikhotel.service.impl;

import com.yener.fistikhotel.model.Booking;
import com.yener.fistikhotel.model.mapper.BookingMapper;
import com.yener.fistikhotel.model.response.BookingResponse;
import com.yener.fistikhotel.repository.BookingRepository;
import com.yener.fistikhotel.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository roomRepository;
    @Override
    public List<BookingResponse> getAllBookedRooms() {
        List<Booking> bookings = roomRepository.findAll();
        return BookingMapper.BOOKING_MAPPER.toResponseList(bookings);
    }
}
