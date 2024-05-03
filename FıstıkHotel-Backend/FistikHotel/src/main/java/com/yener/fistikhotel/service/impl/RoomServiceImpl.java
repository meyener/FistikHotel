package com.yener.fistikhotel.service.impl;

import com.yener.fistikhotel.exception.InternalServerException;
import com.yener.fistikhotel.exception.ResourceNotFoundException;
import com.yener.fistikhotel.exception.RoomException;
import com.yener.fistikhotel.model.Room;
import com.yener.fistikhotel.model.mapper.RoomMapper;
import com.yener.fistikhotel.model.response.RoomResponse;
import com.yener.fistikhotel.repository.RoomRepository;
import com.yener.fistikhotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {


    private final RoomRepository roomRepository;

    @Override
    public RoomResponse addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws SQLException, IOException {

        byte[] photoBytes = photo.getBytes();
        Room saved = getPreparedRoom(roomType, roomPrice, photoBytes);

        return RoomMapper.ROOM_MAPPER.toResponse(saved, saved.getBookings());
    }

    @Override
    public List<String> getRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        Room theRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Sorry, Room not found!"));

        Blob photoBlob = theRoom.getPhoto();
        if (photoBlob != null) {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return new byte[0];
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream().map(obj -> {
            try {
                return RoomMapper.ROOM_MAPPER.toResponse(obj, obj.getBookings());
            } catch (SQLException | IOException e) {
                throw new RoomException("getAllRooms error room: " + obj);
            }
        }).toList();
    }

    @Override
    public void deleteRoom(Long roomId)  {
        getRoomById(roomId);
        roomRepository.deleteById(roomId);

    }

    @Override
    public Room getRoomById(Long roomId)  {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException("Room is not exist id :"+roomId));
    }


    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, MultipartFile photo) throws IOException, SQLException {

        byte[] photoBytes = photo != null && !photo.isEmpty() ?
                photo.getBytes() : getRoomPhotoByRoomId(roomId);

        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;

        Room room = checkAndReturnRoom(roomId,roomType,roomPrice,photoBytes);
        room.setPhoto(photoBlob);
        return room;
    }


    @Override
    public List<RoomResponse> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        List<Room> rooms = roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);
        return RoomMapper.ROOM_MAPPER.roomResponseList(rooms);
    }

    private Room checkAndReturnRoom(Long roomId,String roomType, BigDecimal roomPrice,byte[] photoBytes) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomException("Roo"));

        if (roomType != null) {
            room.setRoomType(roomType);
        }
        if (roomPrice != null) {
            room.setRoomPrice(roomPrice);
        }
        if (photoBytes != null && photoBytes.length > 0) {
            try {
                room.setPhoto(new SerialBlob(photoBytes));
            } catch (SQLException ex) {
                throw new InternalServerException("Fail updating room");
            }
        }
        return room;
    }

    private Room getPreparedRoom(String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        Blob photoBlob;

        try {
            photoBlob = new SerialBlob(photoBytes);
        } catch (Exception e) {
            throw new RoomException(e.getMessage());
        }

        Room room = Room.builder()
                .roomType(roomType)
                .roomPrice(roomPrice)
                .photo(photoBlob)
                .build();

        return roomRepository.save(room);
    }
}
