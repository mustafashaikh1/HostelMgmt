package com.Hostel.Controller;



import com.Hostel.Entity.HostelSlideBar;
import com.Hostel.Service.HostelSlideBarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/public")
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class HostelSlideBarController {

    @Autowired
    private HostelSlideBarService hostelSlideBarService;

    // ✅ Create HostelSlideBar
    @PostMapping("/createHostelSlideBar")
    public ResponseEntity<?> createHostelSlideBar(@RequestParam String slideBarColor,
                                                  @RequestParam(required = false) List<MultipartFile> slideImages) {
        try {
            HostelSlideBar hostelSlideBar = new HostelSlideBar();
            hostelSlideBar.setSlideBarColor(slideBarColor);

            HostelSlideBar createdHostelSlideBar = hostelSlideBarService.createHostelSlideBar(hostelSlideBar, slideImages);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHostelSlideBar);
        } catch (IOException e) {
            log.error("Failed to create HostelSlideBar: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create HostelSlideBar: " + e.getMessage());
        }
    }
    @PutMapping("/updateHostelSlideBarImage/{slideBarId}")
    public ResponseEntity<?> updateHostelSlideBarImageById(@PathVariable Long slideBarId,
                                                           @RequestParam (required = false) MultipartFile newImage,
                                                           @RequestParam(required = false) String slideBarColor,
                                                           @RequestParam(required = false) Long imageId) {
        try {
            HostelSlideBar updated = hostelSlideBarService
                    .updateHostelSlideBarImageById(slideBarId, newImage, slideBarColor, imageId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("Update failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Update failed: " + e.getMessage());
        } catch (IOException e) {
            log.error("Image upload error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Image upload error: " + e.getMessage());
        }
    }





    // ✅ Delete Specific Image from HostelSlideBar
    @DeleteMapping("/deleteHostelSlideBar/{id}")
    public ResponseEntity<String> deleteHostelSlideBar(@PathVariable Long id) {
        try {
            hostelSlideBarService.deleteHostelSlideBar(id);
            return ResponseEntity.ok("HostelSlideBar with ID " + id + " deleted successfully. Images remain in S3.");
        } catch (RuntimeException e) {
            log.error("HostelSlideBar deletion failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("HostelSlideBar deletion failed: " + e.getMessage());
        }
    }



    // ✅ Get HostelSlideBar by ID
    @GetMapping("/getHostelSlideBarById/{id}")
    public ResponseEntity<Optional<HostelSlideBar>> getHostelSlideBarById(@PathVariable Long id) {
        Optional<HostelSlideBar> hostelSlideBar = hostelSlideBarService.getHostelSlideBarById(id);
        return ResponseEntity.ok(hostelSlideBar);
    }

    // ✅ NEW API: Get All Hostel SlideBars
    @GetMapping("/getAllHostelSlideBars")
    public ResponseEntity<?> getAllHostelSlideBars() {
        try {
            List<HostelSlideBar> slideBars = hostelSlideBarService.getAllHostelSlideBars();
            if (slideBars.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Hostel SlideBars found.");
            }
            return ResponseEntity.ok(slideBars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching Hostel SlideBars: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteSlideImage/{slideBarId}/{imageId}")
    public ResponseEntity<?> deleteSlideImageByImageId(@PathVariable Long slideBarId,
                                                       @PathVariable Long imageId) {
        try {
            HostelSlideBar updated = hostelSlideBarService.deleteSlideImageByImageId(slideBarId, imageId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("Failed to delete slide image: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete image: " + e.getMessage());
        }
    }

}
