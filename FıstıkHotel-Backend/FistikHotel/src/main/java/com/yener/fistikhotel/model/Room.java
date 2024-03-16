package com.yener.fistikhotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGeneratorFactory;

@Entity
@Data
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked=false;
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<BookedRoom> bookings;
    @Lob
    private Blob photo;

    public Room() {
        this.bookings = new ArrayList<>();
    }

    public void addBooking(BookedRoom bookedRoom) {
        if (bookings == null) {
            bookings=new ArrayList<>();
        }
        bookings.add(bookedRoom);
        bookedRoom.setRoom(this);
        isBooked=true;
        String bookingCode = RandomStringUtils.randomNumeric(10);
        bookedRoom.setBookingConfirmationCode(bookingCode);
    }
}
