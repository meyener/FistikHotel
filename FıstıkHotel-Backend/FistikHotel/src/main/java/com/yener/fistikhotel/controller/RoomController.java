package com.yener.fistikhotel.controller;


import com.yener.fistikhotel.model.Room;
import com.yener.fistikhotel.model.mapper.RoomMapper;
import com.yener.fistikhotel.model.response.RoomResponse;
import com.yener.fistikhotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin("http://localhost:5173/")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/add/new-room")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {
        RoomResponse savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        return ResponseEntity.ok(savedRoom);
    }

    @GetMapping("/room/types")
    public List<String> getRoomTypes() {
        return roomService.getRoomTypes();
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @DeleteMapping("/delete/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) throws SQLException, IOException {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long roomId) throws SQLException, IOException {
        Room room = roomService.getRoomById(roomId);
        RoomResponse response = RoomMapper.ROOM_MAPPER.toResponse(room, room.getBookings());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) MultipartFile photo) throws SQLException, IOException {
        Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photo);
        RoomResponse response = RoomMapper.ROOM_MAPPER.toResponse(theRoom, theRoom.getBookings());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate checkOutDate,
            @RequestParam("roomType") String roomType) {
        List<RoomResponse> availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);
        return ResponseEntity.ok(availableRooms);
        }
}
