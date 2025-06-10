package com.Hostel.Controller;



import com.Hostel.Entity.HostelInquiry;
import com.Hostel.Service.HostelFormService;
import com.Hostel.Service.HostelInquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

//@CrossOrigin("http://localhost:3000")
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
@RequestMapping("/public/hostel-inquiry")

public class HostelInquiryController {

    @Autowired
    private HostelInquiryService hostelInquiryService;

    @Autowired
    private HostelFormService hostelFormService;

    // Create a new Hostel Inquiry
    @PostMapping("/create")
    public ResponseEntity<HostelInquiry> createHostelInquiry(@RequestBody HostelInquiry hostelInquiry) {
        HostelInquiry savedInquiry = hostelInquiryService.saveHostelInquiry(hostelInquiry);
        return ResponseEntity.ok(savedInquiry);
    }

    // Get all Hostel Inquiries
    @GetMapping("/getAll")
    public ResponseEntity<List<HostelInquiry>> getAllHostelInquiries() {
        return ResponseEntity.ok(hostelInquiryService.getAllHostelInquiries());
    }

    // Get Hostel Inquiry by ID
    @GetMapping("/get/{inquiryId}")
    public ResponseEntity<HostelInquiry> getHostelInquiryById(@PathVariable Long inquiryId) {
        return ResponseEntity.ok(hostelInquiryService.getHostelInquiryById(inquiryId));
    }

    // Get Hostel Inquiries by Student Name
    @GetMapping("/getByStudentName/{studentName}")
    public ResponseEntity<List<HostelInquiry>> getHostelInquiriesByStudentName(@PathVariable String studentName) {
        return ResponseEntity.ok(hostelInquiryService.getHostelInquiriesByStudentName(studentName));
    }

    // Get Hostel Inquiries by City
    @GetMapping("/getByCity/{city}")
    public ResponseEntity<List<HostelInquiry>> getHostelInquiriesByCity(@PathVariable String city) {
        return ResponseEntity.ok(hostelInquiryService.getHostelInquiriesByCity(city));
    }

    // Get Hostel Inquiries by Current Status



    // Get Hostel Inquiries by Email
    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<List<HostelInquiry>> getHostelInquiriesByEmail(@PathVariable String email) {
        return ResponseEntity.ok(hostelInquiryService.getHostelInquiriesByEmail(email));
    }

    // Update Hostel Inquiry
    @PutMapping("/update/{inquiryId}")
    public ResponseEntity<HostelInquiry> updateHostelInquiry(
            @PathVariable Long inquiryId,
            @RequestBody HostelInquiry hostelInquiry) {
        HostelInquiry updatedInquiry = hostelInquiryService.updateHostelInquiry(inquiryId, hostelInquiry);
        return ResponseEntity.ok(updatedInquiry);
    }

    // Delete Hostel Inquiry
    @DeleteMapping("/delete/{inquiryId}")
    public ResponseEntity<String> deleteHostelInquiry(@PathVariable Long inquiryId) {
        hostelInquiryService.deleteHostelInquiry(inquiryId);
        return ResponseEntity.ok("Inquiry deleted successfully");
    }




    // ✅ Update Inquiry Status
    @PutMapping("/updateStatus/{inquiryId}/{newStatus}")
    public ResponseEntity<?> updateInquiryStatus(@PathVariable Long inquiryId, @PathVariable String newStatus) {
        try {
            HostelInquiry updatedInquiry = hostelInquiryService.updateInquiryStatus(inquiryId, newStatus);
            return ResponseEntity.ok(updatedInquiry);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating status: " + e.getMessage());
        }
    }
    @GetMapping("/getByCurrentStatus/{currentStatus}")
    public ResponseEntity<List<HostelInquiry>> getHostelInquiriesByCurrentStatus(@PathVariable String currentStatus) {
        return ResponseEntity.ok(hostelInquiryService.getHostelInquiriesByCurrentStatus(currentStatus));
    }

    @GetMapping("/getInquiryStats")
    public ResponseEntity<Map<String, Object>> getInquiryStats() {
        return ResponseEntity.ok(hostelInquiryService.getInquiryStats());
    }


    @GetMapping("/getMonthlyInquiryStats")
    public ResponseEntity<Map<String, Object>> getMonthlyInquiryStats(@RequestParam int year) {
        try {
            Map<String, Object> response = hostelInquiryService.getMonthlyInquiryStats(year, "/api/hostelForm/getMonthlyInquiryStats");
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message", ex.getReason()

                    ));
        }
    }
    @GetMapping("/inquiriesSourceWise")
    public ResponseEntity<Map<String, Long>> getSourceWiseInquiries(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, Long> result = hostelInquiryService.getSourceWiseInquiries(startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getInquirySummaryMonthAndYear")
    public ResponseEntity<List<Map<String, Object>>> getInquirySummary(
            @RequestParam String monthName,
            @RequestParam String year) {

        List<Object[]> results = hostelInquiryService.getInquirySummaryByMonthAndYear(monthName, year);

        List<Map<String, Object>> response = results.stream().map(record -> {
            Map<String, Object> map = new HashMap<>();
            map.put("inquiryDate", record[0]);
            map.put("count", record[1]);
            return map;
        }).toList();

        return ResponseEntity.ok(response);
    }

}