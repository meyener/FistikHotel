package com.yener.fistikhotel.model.response;

import lombok.*;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponse {

    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked;
    private List<BookingResponse> bookings;
    private String photo  ;

    public static RoomResponse returnRoomResponseByOnly6params(Long id, String roomType, BigDecimal roomPrice, boolean isBooked, byte[] photoBytes, List<BookingResponse> bookingInfo){
        return RoomResponse.builder()
                .id(id)
                .roomType(roomType)
                .roomPrice(roomPrice)
                .isBooked(isBooked)
                .photo(Base64.encodeBase64String(photoBytes))
                .bookings(bookingInfo)
                .build();
    }

    public static RoomResponse returnRoomResponseByOnly3params(Long id, String roomType, BigDecimal roomPrice){
        return RoomResponse.builder()
                .id(id)
                .roomType(roomType)
                .roomPrice(roomPrice)
                .build();
    }

}
