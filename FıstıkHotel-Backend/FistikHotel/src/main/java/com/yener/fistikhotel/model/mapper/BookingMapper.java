package com.yener.fistikhotel.model.mapper;

import com.yener.fistikhotel.exception.InvalidBookingRequestException;
import com.yener.fistikhotel.model.Booking;
import com.yener.fistikhotel.model.response.BookingResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BookingMapper {

    public static final BookingMapper BOOKING_MAPPER=new BookingMapper();

    private BookingMapper(){}

    public BookingResponse toResponse(Booking booking) throws SQLException, IOException {

        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .bookingConfirmationCode(booking.getBookingConfirmationCode())
                .numOfAdults(booking.getNumOfAdults())
                .numOfChildren(booking.getNumOfChildren())
                .room(RoomMapper.ROOM_MAPPER.toResponse(booking.getRoom(), booking.getRoom().getBookings()))
                .guestEmail(booking.getGuestEmail())
                .totalNumOfGuest(booking.getTotalNumOfGuest())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .guestFullName(booking.getGuestFullName())
                .build();

    }

    public List<BookingResponse> toResponseList(List<Booking> bookings){
        return bookings.stream().map(obj-> {
            try {
                return toResponse(obj);
            } catch (SQLException | IOException e) {
                throw new InvalidBookingRequestException(e.getCause().getMessage());
            }
        }).toList();
    }

}
