package com.Hostel.Controller;

import com.Hostel.Entity.HostelManuBar;
import com.Hostel.Service.HostelManuBarService;
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
public class HostelManuBarController {

    @Autowired
    private HostelManuBarService hostelManuBarService;

    // ✅ Create a new HostelManuBar entry
    @PostMapping("/createHostelManuBar")
    public ResponseEntity<?> createHostelManuBar(
            @RequestParam String hostelManuBarColor,
            @RequestParam(required = false) MultipartFile hostelManubarImage) {
        try {
            HostelManuBar hostelManuBar = new HostelManuBar();
            hostelManuBar.setHostelManuBarColor(hostelManuBarColor);

            HostelManuBar createdHostelManuBar = hostelManuBarService.createHostelManuBar(hostelManuBar, hostelManubarImage);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHostelManuBar);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error uploading image: " + e.getMessage());
        }
    }


    @PutMapping("/updateHostelManuBar/{id}")
    public ResponseEntity<?> updateHostelManuBar(
            @PathVariable Long id,
            @RequestParam String hostelManuBarColor,
            @RequestParam(required = false) MultipartFile hostelManubarImage) {
        try {
            HostelManuBar updatedHostelManuBar = hostelManuBarService.updateHostelManuBar(id, hostelManuBarColor, hostelManubarImage);
            return ResponseEntity.ok(updatedHostelManuBar);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Error: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error updating HostelManuBar: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Unexpected Error: " + e.getMessage());
        }
    }

    // ✅ Get a single HostelManuBar by ID
    @GetMapping("/getHostelManuBarById/{id}")
    public ResponseEntity<?> getHostelManuBarById(@PathVariable Long id) {
        try {
            HostelManuBar hostelManuBar = hostelManuBarService.getHostelManuBarById(id);
            return ResponseEntity.ok(hostelManuBar);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Error: " + e.getMessage());
        }
    }

    // ✅ Get all HostelManuBars
    @GetMapping("/getAllHostelManuBars")
    public ResponseEntity<List<HostelManuBar>> getAllHostelManuBars() {
        List<HostelManuBar> hostelManuBars = hostelManuBarService.getAllHostelManuBars();
        return ResponseEntity.ok(hostelManuBars);
    }




    // ✅ Delete HostelManuBar (Does NOT delete image from S3)
    @DeleteMapping("/deleteHostelManuBar/{id}")
    public ResponseEntity<String> deleteHostelManuBar(@PathVariable Long id) {
        try {
            hostelManuBarService.deleteHostelManuBar(id);
            return ResponseEntity.ok("✅ HostelManuBar deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Error: " + e.getMessage());
        }
    }
}
