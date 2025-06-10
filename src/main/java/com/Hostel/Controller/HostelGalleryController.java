package com.Hostel.Controller;

import com.Hostel.Entity.HostelGallery;
import com.Hostel.Service.HostelGalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
public class HostelGalleryController {

    @Autowired
    private HostelGalleryService hostelGalleryService;

    // Upload a single photo
    @PostMapping("/uploadPhoto")
    public ResponseEntity<HostelGallery> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description) {
        try {
            HostelGallery photo = hostelGalleryService.uploadSinglePhoto(file, description);
            return ResponseEntity.status(HttpStatus.CREATED).body(photo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Upload multiple photos
    @PostMapping("/uploadMultiplePhotos")
    public ResponseEntity<List<HostelGallery>> uploadMultiplePhotos(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "descriptions", required = false) List<String> descriptions) {
        try {
            if (descriptions == null || descriptions.size() < files.size()) {
                descriptions = new ArrayList<>();
                for (int i = 0; i < files.size(); i++) {
                    descriptions.add("No Description"); // Default description if not provided
                }
            }
            List<HostelGallery> uploadedPhotos = hostelGalleryService.uploadMultiplePhotos(files, descriptions);
            return ResponseEntity.status(HttpStatus.CREATED).body(uploadedPhotos);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/updatePhoto/{id}")
    public ResponseEntity<?> updatePhoto(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("description") String description) {
        try {
            HostelGallery updatedPhoto = hostelGalleryService.updatePhoto(id, file, description);
            return ResponseEntity.ok(updatedPhoto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Photo not found with ID: " + id);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error updating file: " + e.getMessage());
        }
    }




    // Get all photos
    @GetMapping("/getAllPhotos")
    public ResponseEntity<List<HostelGallery>> getAllPhotos() {
        return ResponseEntity.ok(hostelGalleryService.getAllPhotos());
    }

    // Get a photo by ID
    @GetMapping("/getPhotoById/{id}")
    public ResponseEntity<HostelGallery> getPhotoById(@PathVariable Long id) {
        return ResponseEntity.ok(hostelGalleryService.getPhotoById(id));
    }

    @DeleteMapping("/deletePhoto/{id}")
    public ResponseEntity<String> deletePhoto(@PathVariable Long id) {
        try {
            hostelGalleryService.deletePhoto(id);
            return ResponseEntity.ok("✅ Photo record deleted, but image remains in S3.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Photo not found with ID: " + id);
        }
    }


}
