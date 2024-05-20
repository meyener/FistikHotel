package com.yener.fistikhotel.service.impl;

import com.yener.fistikhotel.model.Booking;
import com.yener.fistikhotel.model.Room;
import com.yener.fistikhotel.model.response.BookingResponse;
import com.yener.fistikhotel.repository.BookingRepository;
import com.yener.fistikhotel.service.RoomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class BookingServiceImplTest {
    @Mock
    BookingRepository bookingRepository;
    @Mock
    RoomService roomService;
    @InjectMocks
    BookingServiceImpl bookingServiceImpl;

    Booking booking;

    Booking secBooking;

    List<Booking> bookings=new ArrayList<>();
    List<Booking> secBookings=new ArrayList<>();

    BookingResponse response;
    List<BookingResponse> responses;

    Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingServiceImpl=new BookingServiceImpl(bookingRepository,roomService);
        booking= Mockito.mock(Booking.class);
        booking=new Booking();
        secBooking=new Booking();
        room=new Room();
        room.setId(1L);
        booking.setRoom(room);
        booking.setBookingId(1L);
        booking.setBookingConfirmationCode("123");
        booking.setGuestEmail("123");
        booking.setCheckOutDate(LocalDate.of(2024, Month.MAY, 15));
        booking.setCheckInDate(LocalDate.of(2024, Month.MAY, 10));
        secBooking.setCheckOutDate(LocalDate.of(2024, Month.MAY, 7));
        secBooking.setCheckInDate(LocalDate.of(2024, Month.MAY, 1));
        bookings.add(booking);
        secBookings.add(secBooking);
        secBooking.setRoom(room);
        room.setBookings(secBookings);
        response=new BookingResponse();
        response.setBookingId(1L);
        response.setCheckOutDate(LocalDate.of(2024, Month.MAY, 15));
        response.setCheckInDate(LocalDate.of(2024, Month.MAY, 10));
        responses=List.of(response);
    }

    @Test
    void testGetAllBookings() {
        when(bookingRepository.findAll()).thenReturn(bookings);
        responses=bookingServiceImpl.getAllBookings();
        Assertions.assertNotNull(responses);
      }

    @Test
    void testSaveBooking() {
        when(roomService.getRoomById(1L)).thenReturn(room);

        String result = bookingServiceImpl.saveBooking(1L,booking);
        Assertions.assertEquals(booking.getBookingConfirmationCode(), result);
    }

    @Test
    void testCancelBooking() {
        Long bookingId = 1L;
        doNothing().when(bookingRepository).deleteById(bookingId);

        bookingServiceImpl.cancelBooking(bookingId);
        verify(bookingRepository).deleteById(bookingId);
    }

    @Test
    void testFindByBookingConfirmationCode() {
        Optional<Booking> optionalBooking = Optional.of(secBooking);
        when(bookingRepository.findByBookingConfirmationCode(anyString())).thenReturn(optionalBooking);
        when(roomService.getRoomById(1L)).thenReturn(room);

        BookingResponse result = bookingServiceImpl.findByBookingConfirmationCode("123");
        Assertions.assertNotNull( result);
    }

    @Test
    void testGetBookingsByUserEmail() {
        when(bookingRepository.findByGuestEmail("123")).thenReturn(bookings);
        when(roomService.getRoomById(1L)).thenReturn(room);

        List<BookingResponse> result = bookingServiceImpl.getBookingsByUserEmail("123");
        Assertions.assertNotNull(result);}

    @Test
    void testGetAllBookingsByRoomId() {
        when(bookingRepository.findByRoomId(anyLong())).thenReturn(bookings);
        List<Booking> result = bookingServiceImpl.getAllBookingsByRoomId(1L);

        Assertions.assertNotNull(result);

    }
}
