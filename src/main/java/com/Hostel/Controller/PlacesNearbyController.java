package com.Hostel.Controller;

import com.Hostel.Entity.PlacesNearby;
import com.Hostel.Service.PlacesNearbyService;
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
@RequestMapping("/public/placesNearby")
public class PlacesNearbyController {

    @Autowired
    private PlacesNearbyService placesNearbyService;

    // ✅ Add a new place (with optional image)
    @PostMapping("/add")
    public ResponseEntity<PlacesNearby> addPlace(
            @RequestParam("place") String place,
            @RequestParam("placeName") String placeName,
            @RequestParam("distance") String distance,
            @RequestParam("travelTime") String travelTime,
            @RequestParam("description") String description,
            @RequestParam(value = "placeImage", required = false) MultipartFile placeImage) {
        try {
            PlacesNearby placesNearby = new PlacesNearby();
            placesNearby.setPlace(place);
            placesNearby.setPlaceName(placeName);
            placesNearby.setDistance(distance);
            placesNearby.setTravelTime(travelTime);
            placesNearby.setDescription(description);

            PlacesNearby savedPlace = placesNearbyService.addPlace(placesNearby, placeImage);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPlace);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ✅ Get all places
    @GetMapping("/all")
    public ResponseEntity<List<PlacesNearby>> getAllPlaces() {
        return ResponseEntity.ok(placesNearbyService.getAllPlaces());
    }

    // ✅ Get place by ID
    @GetMapping("/{placesNearbyId}")
    public ResponseEntity<PlacesNearby> getPlaceById(@PathVariable Long placesNearbyId) {
        return ResponseEntity.ok(placesNearbyService.getPlaceById(placesNearbyId));
    }

    // ✅ Get places by category (e.g., "Railway Station", "Hospital")
    @GetMapping("/category/{place}")
    public ResponseEntity<List<PlacesNearby>> getPlacesByCategory(@PathVariable String place) {
        return ResponseEntity.ok(placesNearbyService.getPlacesByCategory(place));
    }

    // ✅ Update a place (with optional new image)
    @PutMapping("/updatePlace/{placesNearbyId}")
    public ResponseEntity<?> updatePlace(
            @PathVariable Long placesNearbyId,
            @RequestParam("place") String place,
            @RequestParam("placeName") String placeName,
            @RequestParam("distance") String distance,
            @RequestParam("travelTime") String travelTime,
            @RequestParam("description") String description,
            @RequestParam(value = "placeImage", required = false) MultipartFile placeImage) {
        try {
            PlacesNearby placesNearby = new PlacesNearby();
            placesNearby.setPlace(place);
            placesNearby.setPlaceName(placeName);
            placesNearby.setDistance(distance);
            placesNearby.setTravelTime(travelTime);
            placesNearby.setDescription(description);

            PlacesNearby updatedPlace = placesNearbyService.updatePlace(placesNearbyId, placesNearby, placeImage);
            return ResponseEntity.ok(updatedPlace);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Error: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Image update failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Unexpected error: " + e.getMessage());
        }
    }


    @DeleteMapping("/deletePlace/{placesNearbyId}")
    public ResponseEntity<?> deletePlace(@PathVariable Long placesNearbyId) {
        try {
            placesNearbyService.deletePlace(placesNearbyId);
            return ResponseEntity.ok("✅ Place deleted successfully, but image remains in S3.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Unexpected error: " + e.getMessage());
        }
    }

}
