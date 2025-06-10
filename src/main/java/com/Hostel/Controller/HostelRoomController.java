package com.Hostel.Controller;

import com.Hostel.Entity.HostelRoom;
import com.Hostel.Service.FloorService;
import com.Hostel.Service.HostelRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class HostelRoomController {

    private final HostelRoomService hostelRoomService;

    @Autowired
    private FloorService floorService;

    public HostelRoomController(HostelRoomService hostelRoomService) {
        this.hostelRoomService = hostelRoomService;
    }

    // ✅ Add Room to Floor
    @PostMapping("/addRoomToFloor/{floorId}")
    public ResponseEntity<?> addRoomToFloor(@PathVariable Long floorId, @RequestBody HostelRoom hostelRoom) {
        try {
            HostelRoom addedRoom = hostelRoomService.addRoomToFloor(floorId, hostelRoom);
            return ResponseEntity.ok(addedRoom);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Room number already exists on this floor."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "An unexpected error occurred."));
        }
    }


    // ✅ Update Room
    @PutMapping("/updateRoom/{roomId}")
    public ResponseEntity<?> updateRoom(@PathVariable Long roomId, @RequestBody HostelRoom hostelRoom) {
        try {
            HostelRoom updatedRoom = hostelRoomService.updateRoom(roomId, hostelRoom);
            return ResponseEntity.ok(updatedRoom);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "An unexpected error occurred."));
        }
    }


    // ✅ Get All Rooms by Floor
    @GetMapping("/getRoomsByFloor/{floorId}")
    public ResponseEntity<?> getRoomsByFloor(@PathVariable Long floorId) {
        try {
            List<HostelRoom> rooms = hostelRoomService.getRoomsByFloor(floorId);
            if (rooms.isEmpty()) {
                return ResponseEntity.ok(Map.of("message", "No rooms found on this floor."));
            }
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "An unexpected error occurred."));
        }
    }

    // ✅ Get Room by ID
    @GetMapping("/getRoomById/{roomId}")
    public ResponseEntity<?> getRoomById(@PathVariable Long roomId) {
        try {
            HostelRoom room = hostelRoomService.getRoomById(roomId);
            return ResponseEntity.ok(room);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "An unexpected error occurred."));
        }
    }

    // ✅ Delete Room
    @DeleteMapping("/deleteHostelRoom/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
        try {
            hostelRoomService.deleteRoom(roomId);
            return ResponseEntity.ok(Map.of("message", "Room deleted successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "An unexpected error occurred."));
        }
    }
}
