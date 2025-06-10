package com.Hostel.Controller;

import com.Hostel.Entity.HostelAgreement;
import com.Hostel.Service.HostelAgreementService;
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
public class HostelAgreementController {

    private final HostelAgreementService hostelAgreementService;

    public HostelAgreementController(HostelAgreementService hostelAgreementService) {
        this.hostelAgreementService = hostelAgreementService;
    }

    @PostMapping("/saveAgreement")
    public ResponseEntity<?> saveAgreement(
            @RequestParam String formNumber,
            @RequestParam String applicantName,
            @RequestParam(required = false) MultipartFile applicantSignature,
            @RequestParam String parentName,
            @RequestParam(required = false) MultipartFile parentSignature)  {
        try {
            HostelAgreement agreement = hostelAgreementService.saveHostelAgreement(formNumber, applicantName, applicantSignature, parentName, parentSignature);
            return ResponseEntity.ok(agreement);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to upload signatures.");
        }
    }


    // Fetch Hostel Agreement by ID
    @GetMapping("/getAgreementById/{id}")
    public ResponseEntity<HostelAgreement> getAgreementById(@PathVariable Long id) {
        return ResponseEntity.ok(hostelAgreementService.getHostelAgreementById(id));
    }

    // Fetch Hostel Agreement by form number (linked with HostelForm)
    @GetMapping("/getAgreementByFormNumber/{formNumber}")
    public ResponseEntity<HostelAgreement> getAgreementByFormNumber(@PathVariable String formNumber) {
        return ResponseEntity.ok(hostelAgreementService.getHostelAgreementByFormNumber(formNumber));
    }

    @GetMapping("/getAllAgreements")
    public ResponseEntity<List<HostelAgreement>> getAllAgreements() {
        return ResponseEntity.ok(hostelAgreementService.getAllHostelAgreements());
    }

    @PutMapping("/updateAgreement/{id}")
    public ResponseEntity<HostelAgreement> updateAgreement(
            @PathVariable Long id,
            @RequestParam String applicantName,
            @RequestParam(required = false) MultipartFile applicantSignature,
            @RequestParam String parentName,
            @RequestParam(required = false) MultipartFile parentSignature) {
        try {
            HostelAgreement agreement = hostelAgreementService.updateHostelAgreement(id, applicantName, applicantSignature, parentName, parentSignature);
            return ResponseEntity.ok(agreement);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Delete Hostel Agreement (Database only, images remain in S3)
    @DeleteMapping("/deleteHostelAgreement/{formNumber}")
    public ResponseEntity<String> deleteHostelAgreement(@PathVariable String formNumber) {
        hostelAgreementService.deleteHostelAgreement(formNumber);
        return ResponseEntity.ok("Hostel Agreement deleted successfully, but images remain in S3.");
    }
}
