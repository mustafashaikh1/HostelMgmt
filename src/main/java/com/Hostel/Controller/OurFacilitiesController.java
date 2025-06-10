package com.Hostel.Controller;

import com.Hostel.Entity.OurFacilities;
import com.Hostel.Service.OurFacilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
public class OurFacilitiesController {

    @Autowired
    private OurFacilitiesService ourFacilitiesService;

    // ✅ Add a new facility
    @PostMapping("/addFacility")
    public ResponseEntity<OurFacilities> addFacility(
            @RequestParam("facilityName") String facilityName,
            @RequestParam("description") String description,
            @RequestParam(value = "facilityImage", required = false) MultipartFile facilityImage) {
        try {
            OurFacilities facility = ourFacilitiesService.addFacility(facilityName, description, facilityImage);
            return ResponseEntity.status(HttpStatus.CREATED).body(facility);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ✅ Get all facilities
    @GetMapping("/getAllFacilities")
    public ResponseEntity<List<OurFacilities>> getAllFacilities() {
        return ResponseEntity.ok(ourFacilitiesService.getAllFacilities());
    }

    // ✅ Get facility by ID
    @GetMapping("/getFacilityById/{facilityId}")
    public ResponseEntity<OurFacilities> getFacilityById(@PathVariable Long facilityId) {
        return ResponseEntity.ok(ourFacilitiesService.getFacilityById(facilityId));
    }

    // ✅ Update facility
    @PutMapping("/updateFacility/{facilityId}")
    public ResponseEntity<OurFacilities> updateFacility(
            @PathVariable Long facilityId,
            @RequestParam("facilityName") String facilityName,
            @RequestParam("description") String description,
            @RequestParam(value = "facilityImage", required = false) MultipartFile facilityImage) {
        try {
            OurFacilities updatedFacility = ourFacilitiesService.updateFacility(facilityId, facilityName, description, facilityImage);
            return ResponseEntity.ok(updatedFacility);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ✅ Delete facility by ID
    @DeleteMapping("/deleteFacility/{facilityId}")
    public ResponseEntity<String> deleteFacility(@PathVariable Long facilityId) {
        ourFacilitiesService.deleteFacility(facilityId);
        return ResponseEntity.ok("Facility deleted successfully!");
    }
}
