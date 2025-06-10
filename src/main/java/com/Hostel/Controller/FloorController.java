package com.Hostel.Controller;

import com.Hostel.Dto.FloorResponse;
import com.Hostel.Entity.Floor;
import com.Hostel.Service.FloorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class FloorController {

    private final FloorService floorService;

    public FloorController(FloorService floorService) {
        this.floorService = floorService;
    }

    // ✅ Create a new floor with manually entered totalRooms
    @PostMapping("/createFloor")
    public ResponseEntity<Floor> createFloor(
            @RequestParam String hostelBuildingName,
            @RequestParam String floorName,
            @RequestParam int totalRooms,
            @RequestParam Long adminId) {

        Floor floor = floorService.createFloor(floorName, totalRooms, hostelBuildingName, adminId);
        return ResponseEntity.ok(floor);
    }


    // ✅ Get all floors
    @GetMapping("/getAllFloors")
    public ResponseEntity<List<Floor>> getAllFloors() {
        List<Floor> floors = floorService.getAllFloors();
        return ResponseEntity.ok(floors);
    }

    // ✅ Get a single floor by ID
    @GetMapping("/getFloorById/{floorId}")
    public ResponseEntity<Floor> getFloorById(@PathVariable Long floorId) {
        Floor floor = floorService.getFloorById(floorId);
        return ResponseEntity.ok(floor);
    }

    // ✅ Update floor details (including totalRooms)
    @PutMapping("/updateFloor/{floorId}")
    public ResponseEntity<Floor> updateFloor(
            @PathVariable Long floorId,
            @RequestParam String floorName,
            @RequestParam int totalRooms,
            @RequestParam String hostelBuildingName) {

        Floor updatedFloor = floorService.updateFloor(floorId, totalRooms, floorName, hostelBuildingName);
        return ResponseEntity.ok(updatedFloor);
    }

    // ✅ Delete a floor by ID
    @DeleteMapping("/deleteFloor/{floorId}")
    public ResponseEntity<String> deleteFloor(@PathVariable Long floorId) {
        floorService.deleteFloor(floorId);
        return ResponseEntity.ok("Floor deleted successfully");
    }

    @GetMapping("/getFloorCapacity/{floorId}")
    public ResponseEntity<FloorResponse> getFloorCapacity(@PathVariable Long floorId) {
        FloorResponse response = floorService.getFloorDetails(floorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getFloorsByBuildingName")
    public ResponseEntity<List<Floor>> getFloorsByBuildingName(@RequestParam String buildingName) {
        List<Floor> floors = floorService.getFloorsByBuildingName(buildingName);
        return ResponseEntity.ok(floors);
    }

}
