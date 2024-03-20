package com.yener.fistikhotel.model.mapper;

import com.yener.fistikhotel.exception.PhotoRetrievalException;
import com.yener.fistikhotel.exception.RoomException;
import com.yener.fistikhotel.model.Booking;
import com.yener.fistikhotel.model.Room;
import com.yener.fistikhotel.model.response.BookingResponse;
import com.yener.fistikhotel.model.response.RoomResponse;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

public class RoomMapper {
    public static final RoomMapper ROOM_MAPPER = new RoomMapper();

    private RoomMapper() {
    }

    public RoomResponse toResponse(Room room, List<Booking> bookings) throws SQLException, IOException {

        List<BookingResponse> bookingInfo = bookings.stream()
                .map(booking -> BookingResponse
                        .returnBookingResponseByOnly4params(
                                booking.getBookingId(),
                                booking.getCheckInDate(),
                                booking.getCheckOutDate(),
                                booking.getBookingConfirmationCode())).toList();

        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return RoomResponse.returnRoomResponseByOnly4params(room.getId(),
                room.getRoomType(), room.getRoomPrice(),
                room.isBooked(), null, bookingInfo);
    }

    public List<RoomResponse> roomResponseList(List<Room> rooms) {
        return rooms.stream().map(obj -> {
            try {
                return RoomMapper.ROOM_MAPPER.toResponse(obj, obj.getBookings());
            } catch (SQLException | IOException e) {
                throw new RoomException(e.getMessage());
            }
        }).toList();
    }


}
