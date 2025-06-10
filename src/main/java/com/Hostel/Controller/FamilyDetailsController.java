package com.Hostel.Controller;

import com.Hostel.Entity.FamilyDetails;
import com.Hostel.Entity.HostelForm;
import com.Hostel.Service.FamilyDetailsService;
import com.Hostel.Service.HostelFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class FamilyDetailsController {

    @Autowired
    private FamilyDetailsService familyDetailsService;

    @Autowired
    private HostelFormService hostelFormService;

    @PostMapping("/addFamilyDetails")
    public ResponseEntity<?> addFamilyDetails(
            @RequestParam String formNumber,
            @RequestBody FamilyDetails familyDetails) {

        try {
            HostelForm hostelForm = hostelFormService.getHostelFormByFormNumber(formNumber);
            FamilyDetails savedFamilyDetails = familyDetailsService.saveFamilyDetails(familyDetails, formNumber);
            hostelForm.setFamilyDetails(savedFamilyDetails);
            hostelFormService.saveHostelForm(hostelForm);
            return ResponseEntity.ok(hostelForm);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "HostelForm not found with formNumber: " + formNumber));
        }
    }

    @GetMapping("/getAllFamilyDetails")
    public ResponseEntity<?> getAllFamilyDetails() {
        try {
            List<FamilyDetails> familyDetailsList = familyDetailsService.getAllFamilyDetails();
            return ResponseEntity.ok(familyDetailsList);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        }
    }

    @GetMapping("/getById/{familyDetailsId}")
    public ResponseEntity<?> getFamilyDetailsById(@PathVariable Long familyDetailsId) {
        try {
            FamilyDetails familyDetails = familyDetailsService.getFamilyDetailsById(familyDetailsId);
            return ResponseEntity.ok(familyDetails);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        }
    }

    @GetMapping("/getFamilyDetailsByCity")
    public ResponseEntity<?> getFamilyDetailsByCity(@RequestParam String city) {
        try {
            List<FamilyDetails> familyDetailsList = familyDetailsService.getFamilyDetailsByCity(city);
            return ResponseEntity.ok(familyDetailsList);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        }
    }

    @PutMapping("/updateFamilyDetails/{familyDetailsId}")
    public ResponseEntity<?> updateFamilyDetails(
            @PathVariable Long familyDetailsId,
            @RequestBody FamilyDetails familyDetails) {
        try {
            FamilyDetails updatedDetails = familyDetailsService.updateFamilyDetails(familyDetailsId, familyDetails);
            return ResponseEntity.ok(updatedDetails);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        }
    }

    @DeleteMapping("/deleteFamilyDetails/{familyDetailsId}")
    public ResponseEntity<?> deleteFamilyDetails(@PathVariable Long familyDetailsId) {
        try {
            familyDetailsService.deleteFamilyDetails(familyDetailsId);
            return ResponseEntity.ok(Map.of("message", "FamilyDetails with ID " + familyDetailsId + " has been deleted."));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        }
    }
}
