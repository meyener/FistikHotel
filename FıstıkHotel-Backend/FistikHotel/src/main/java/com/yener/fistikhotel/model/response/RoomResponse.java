package com.yener.fistikhotel.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class RoomResponse {

    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked;
    private List<BookingResponse> bookings;
    private String photo  ;

    public static RoomResponse returnRoomResponseByOnly4params(Long id, String roomType, BigDecimal roomPrice, boolean isBooked,byte[] photoBytes,List<BookingResponse> bookingInfo){
        return RoomResponse.builder()
                .id(id)
                .roomType(roomType)
                .roomPrice(roomPrice)
                .isBooked(isBooked)
                .photo(Base64.encodeBase64String(photoBytes))
                .bookings(bookingInfo)
                .build();
    }

}
