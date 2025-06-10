package com.Hostel.Controller;


import com.Hostel.Entity.HostelTestimonials;
import com.Hostel.Service.HostelTestimonialsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
public class HostelTestimonialsController {

    @Autowired
    private HostelTestimonialsService testimonialsService;

    @PostMapping("/createTestimonial")
    public ResponseEntity<HostelTestimonials> createTestimonial(
            @RequestParam String testimonialTitle,
            @RequestParam String testimonialName,
            @RequestParam String description,
            @RequestParam String testimonialColor,
            @RequestPart(value = "testimonialImage", required = false) MultipartFile testimonialImage) throws IOException {

        HostelTestimonials testimonial = new HostelTestimonials();
        testimonial.setTestimonialTitle(testimonialTitle);
        testimonial.setTestimonialName(testimonialName);
        testimonial.setDescription(description);
        testimonial.setTestimonialColor(testimonialColor);

        return ResponseEntity.ok(testimonialsService.createTestimonial(testimonial, testimonialImage));
    }

    @PutMapping("/updateTestimonial/{id}")
    public ResponseEntity<HostelTestimonials> updateTestimonial(
            @PathVariable Long id,
            @RequestParam String testimonialTitle,
            @RequestParam String testimonialName,
            @RequestParam String description,
            @RequestParam String testimonialColor,
            @RequestPart(value = "testimonialImage", required = false) MultipartFile testimonialImage) throws IOException {

        HostelTestimonials existingTestimonial = testimonialsService.getTestimonialById(id);

        HostelTestimonials testimonial = new HostelTestimonials();
        testimonial.setTestimonialTitle(testimonialTitle);
        testimonial.setTestimonialName(testimonialName);
        testimonial.setDescription(description);
        testimonial.setTestimonialColor(testimonialColor);

        if (testimonialImage == null || testimonialImage.isEmpty()) {
            testimonial.setTestimonialImage(existingTestimonial.getTestimonialImage());
        }

        return ResponseEntity.ok(testimonialsService.updateTestimonial(id, testimonial, testimonialImage));
    }

    @DeleteMapping("/deleteTestimonialById/{id}")
    public ResponseEntity<String> deleteTestimonialById(@PathVariable Long id) {
        try {
            testimonialsService.deleteTestimonialById(id);
            return ResponseEntity.ok("Testimonial deleted, but image remains in S3.");
        } catch (RuntimeException e) {
            log.error("Testimonial deletion failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Testimonial deletion failed: " + e.getMessage());
        }
    }

    @GetMapping("/getTestimonialById/{id}")
    public ResponseEntity<HostelTestimonials> getTestimonialById(@PathVariable Long id) {
        return ResponseEntity.ok(testimonialsService.getTestimonialById(id));
    }

    @GetMapping("/getAllTestimonials")
    public ResponseEntity<List<HostelTestimonials>> getAllTestimonials() {
        return ResponseEntity.ok(testimonialsService.getAllTestimonials());
    }
}
