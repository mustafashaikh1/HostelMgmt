package com.Hostel.Controller;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Service.HostelFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hostelForm") // Base path for API calls
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class HostelFormController {

    @Autowired
    private HostelFormService hostelFormService;

    // Create a HostelForm
    @PostMapping("/createHostelForm")
    public ResponseEntity<?> createHostelForm(@RequestBody HostelForm hostelForm) {
        try {
            HostelForm savedForm = hostelFormService.saveHostelForm(hostelForm);
            return ResponseEntity.ok(savedForm);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error while creating HostelForm: " + e.getMessage());
        }
    }

    // Get all HostelForms
    @GetMapping("/getAllHostelForms")
    public ResponseEntity<?> getAllHostelForms() {
        try {
            List<HostelForm> hostelForms = hostelFormService.getAllHostelForms();
            return ResponseEntity.ok(hostelForms);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error while fetching HostelForms: " + e.getMessage());
        }
    }

    // Get HostelForm by ID
    @GetMapping("/getHostelFormById/{id}")
    public ResponseEntity<?> getHostelFormById(@PathVariable Long id) {
        try {
            HostelForm hostelForm = hostelFormService.getHostelFormById(id);
            return ResponseEntity.ok(hostelForm);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get HostelForm by FormNumber
    @GetMapping("/getHostelFormByFormNumber")
    public ResponseEntity<?> getHostelFormByFormNumber(@RequestParam("formNumber") String formNumber) {
        try {
            HostelForm hostelForm = hostelFormService.getHostelFormByFormNumber(formNumber);
            return ResponseEntity.ok(hostelForm);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    // Update HostelForm
    @PutMapping("/updateHostelForm/{id}")
    public ResponseEntity<?> updateHostelForm(@PathVariable Long id, @RequestBody HostelForm hostelForm) {
        try {
            HostelForm updatedForm = hostelFormService.updateHostelForm(id, hostelForm);
            return ResponseEntity.ok(updatedForm);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error while updating HostelForm: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteHostelForm(@RequestParam String formNumber) {
        try {
            hostelFormService.deleteHostelFormByFormNumber(formNumber);
            return ResponseEntity.ok("HostelForm with formNumber " + formNumber + " deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while deleting HostelForm: " + e.getMessage());
        }
    }



    @GetMapping("/admission-stats")
    public ResponseEntity<Map<String, Object>> getAdmissionStats() {
        return ResponseEntity.ok(hostelFormService.getAdmissionStats());
    }



    @GetMapping("/getMonthlyStats")
    public ResponseEntity<Map<String, Object>> getMonthlyStats(@RequestParam int year, @RequestHeader("Host") String host) {
        try {
            Map<String, Object> response = hostelFormService.getMonthlyStats(year, "/api/hostelForm/getMonthlyStats");
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message", ex.getReason()

                    ));
        }
    }



}
