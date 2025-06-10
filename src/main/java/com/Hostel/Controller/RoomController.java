package com.Hostel.Controller;

import com.Hostel.Entity.Room;
import com.Hostel.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class RoomController {

    @Autowired
    private RoomService roomService;

    // ✅ Add a new room with images
    @PostMapping("/add/{floorId}")
    public ResponseEntity<Room> addRoom(
            @PathVariable Long floorId,
            @RequestParam("roomType") String roomType,
            @RequestParam("description") String description,
            @RequestParam("images") List<MultipartFile> images) {
        try {
            Room room = roomService.addRoom(floorId, roomType, description, images);
            return ResponseEntity.ok(room);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // ✅ Get all rooms
    @GetMapping("/getAllRooms")
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // ✅ Get room by ID
    @GetMapping("/getRoomById/{roomTypeId}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long roomTypeId) {
        return ResponseEntity.ok(roomService.getRoomById(roomTypeId));
    }

    // ✅ Delete a room by ID
    @DeleteMapping("/deleteRoom/{roomTypeId}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long roomTypeId) {
        roomService.deleteRoom(roomTypeId);
        return ResponseEntity.ok("Room deleted successfully!");
    }

    // ✅ Update an individual image
    @PutMapping("/updateRoomImage")
    public ResponseEntity<Room> updateRoomImage(
            @RequestParam("roomTypeId") Long roomTypeId,
            @RequestParam("imageId") Long imageId,
            @RequestParam("newImage") MultipartFile newImage) {
        try {
            Room updatedRoom = roomService.updateRoomImage(roomTypeId, imageId, newImage);
            return ResponseEntity.ok(updatedRoom);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ✅ Delete an individual image
    @DeleteMapping("/deleteRoomImage")
    public ResponseEntity<Room> deleteRoomImage(
            @RequestParam("roomTypeId") Long roomTypeId,
            @RequestParam("imageId") Long imageId) {
        Room updatedRoom = roomService.deleteRoomImage(roomTypeId, imageId);
        return ResponseEntity.ok(updatedRoom);
    }
}
