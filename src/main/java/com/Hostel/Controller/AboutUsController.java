package com.Hostel.Controller;

import com.Hostel.Entity.AboutUs;
import com.Hostel.Service.AboutUsService;
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
public class AboutUsController {

    @Autowired
    private AboutUsService aboutUsService;

    // ✅ Add About Us content with optional image
    @PostMapping("/addAboutUs")
    public ResponseEntity<?> addAboutUs(
            @RequestParam("description") String description,
            @RequestParam(value = "aboutUsImage", required = false) MultipartFile aboutUsImage) {
        try {
            AboutUs aboutUs = new AboutUs();
            aboutUs.setDescription(description);

            AboutUs newAboutUs = aboutUsService.addAboutUs(aboutUs, aboutUsImage);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAboutUs);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image: " + e.getMessage());
        }
    }

    // ✅ Get all About Us entries
    @GetMapping("/getAllAboutUs")
    public ResponseEntity<List<AboutUs>> getAllAboutUs() {
        List<AboutUs> aboutUsList = aboutUsService.getAllAboutUs();
        return ResponseEntity.ok(aboutUsList);
    }

    // ✅ Get About Us by ID
    @GetMapping("/getAboutUsById/{aboutUsId}")
    public ResponseEntity<?> getAboutUsById(@PathVariable Long aboutUsId) {
        try {
            AboutUs aboutUs = aboutUsService.getAboutUsById(aboutUsId);
            return ResponseEntity.ok(aboutUs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entry not found: " + e.getMessage());
        }
    }

    // ✅ Update About Us entry with optional new image
    @PutMapping("/updateAboutUs/{aboutUsId}")
    public ResponseEntity<?> updateAboutUs(
            @PathVariable Long aboutUsId,
            @RequestParam("description") String description,
            @RequestParam(value = "aboutUsImage", required = false) MultipartFile aboutUsImage) {
        try {
            AboutUs aboutUs = new AboutUs();
            aboutUs.setDescription(description);

            AboutUs updatedAboutUs = aboutUsService.updateAboutUs(aboutUsId, aboutUs, aboutUsImage);
            return ResponseEntity.ok(updatedAboutUs);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error updating image: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Entry not found: " + e.getMessage());
        }
    }


    @DeleteMapping("/deleteAboutUs/{aboutUsId}")
    public ResponseEntity<?> deleteAboutUs(@PathVariable Long aboutUsId) {
        try {
            aboutUsService.deleteAboutUs(aboutUsId);
            return ResponseEntity.ok("✅ About Us entry deleted successfully, but images remain in S3!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Entry not found: " + e.getMessage());
        }
    }

}