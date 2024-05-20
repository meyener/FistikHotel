package com.yener.fistikhotel.service.impl;

import com.yener.fistikhotel.model.Booking;
import com.yener.fistikhotel.model.Room;
import com.yener.fistikhotel.model.response.BookingResponse;
import com.yener.fistikhotel.model.response.RoomResponse;
import com.yener.fistikhotel.repository.RoomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RoomServiceImplTest {
    @Mock
    RoomRepository roomRepository;
    @InjectMocks
    RoomServiceImpl roomServiceImpl;

    @Mock
    MultipartFile photo;
    byte[] photoBytes;
    @Mock
    Blob photoBlob;

    @Mock
    Room room;
    @Mock
    RoomResponse roomResponse;

    Booking secBooking;

    List<Booking> secBookings=new ArrayList<>();
    List<BookingResponse> bookingResponses =new ArrayList<>();
    List<RoomResponse> roomResponses=new ArrayList<>();

    MockMultipartFile file;

    Long roomId = 1L;
    String roomType = "Updated Room";
    BigDecimal roomPrice = BigDecimal.valueOf(150.00);
    LocalDate checkInDate = LocalDate.now();
    LocalDate checkOutDate = checkInDate.plusDays(2);
    @BeforeEach
    void setUp() throws SQLException {
        roomResponse=new RoomResponse();
        MockitoAnnotations.openMocks(this);
        photoBytes = new byte[]{5, 3};
        secBooking=new Booking();
        photoBlob=new SerialBlob(photoBytes);
        room.setId(1L);
        room.setBookings(secBookings);
        room.setRoomType("a");
        room.setRoomPrice(new BigDecimal(0));
        room.setPhoto(photoBlob);
        roomResponse.setId(1L);
        roomResponse.setBookings(bookingResponses);
        roomResponse.setRoomType("a");
        roomResponse.setRoomPrice(new BigDecimal(0));
        roomResponse.setPhoto(photoBlob.toString());
        secBooking.setCheckOutDate(checkOutDate);
        secBooking.setCheckInDate(checkInDate);
        secBookings.add(secBooking);
        roomResponses.add(roomResponse);
        secBooking.setRoom(room);
        file = new MockMultipartFile("photo", "test.jpg", "image/jpeg", "test".getBytes());
    }

    @Test
    void testAddNewRoom() throws SQLException, IOException {
        Room savedRoom = Room.builder()
                .id(1L)
                .roomType(roomType)
                .roomPrice(roomPrice)
                .photo(new javax.sql.rowset.serial.SerialBlob(file.getBytes()))
                .bookings(secBookings)
                .build();

        when(roomRepository.save(any(Room.class))).thenReturn(savedRoom);

        RoomResponse roomResponse = roomServiceImpl.addNewRoom(file, roomType, roomPrice);

        assertNotNull(roomResponse);
        assertEquals(1L, roomResponse.getId());

    }

    @Test
    void testGetRoomTypes() {
        when(roomRepository.findDistinctRoomTypes()).thenReturn(List.of("a"));

        List<String> result = roomServiceImpl.getRoomTypes();
        assertEquals(List.of("a"), result);
    }

    @Test
    void testGetRoomPhotoByRoomId() throws SQLException {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(room.getPhoto()).thenReturn(photoBlob);
        byte[] result = roomServiceImpl.getRoomPhotoByRoomId(1L);

        Assertions.assertArrayEquals(new byte[]{5, 3}, result);
    }

    @Test
    void testGetAllRooms() {

        when(roomRepository.findAll()).thenReturn(List.of(room));

        List<RoomResponse> result = roomServiceImpl.getAllRooms();
        assertEquals(roomResponses.size(), result.size());
    }

    @Test
    void testDeleteRoom() {

        Room room = Room.builder().id(roomId).build();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        roomServiceImpl.deleteRoom(roomId);

        verify(roomRepository, times(1)).findById(roomId);
    }

    @Test
    void testGetRoomById() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        Room result = roomServiceImpl.getRoomById(1L);

        assertEquals(room, result);
    }

    @Test
    void testUpdateRoom() throws SQLException, IOException {
        Room updatedRoom = Room.builder()
                .id(roomId)
                .roomType(roomType)
                .roomPrice(roomPrice)
                .photo(photoBlob)
                .build();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(updatedRoom);

        Room result = roomServiceImpl.updateRoom(roomId, roomType, roomPrice, photo);

        assertNotNull(result);

    }

    @Test
    void testGetAvailableRooms() {
        when(roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType)).thenReturn(List.of(room));
        List<RoomResponse> result = roomServiceImpl.getAvailableRooms(checkInDate, checkOutDate, roomType);

        assertNotNull(result);

    }

}

