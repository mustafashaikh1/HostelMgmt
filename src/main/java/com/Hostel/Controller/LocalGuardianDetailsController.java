package com.Hostel.Controller;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.LocalGuardianDetails;
import com.Hostel.Service.HostelFormService;
import com.Hostel.Service.LocalGuardianDetailsService;
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
public class LocalGuardianDetailsController {

    @Autowired
    private LocalGuardianDetailsService localGuardianDetailsService;

    @Autowired
    private HostelFormService hostelFormService;

    @PostMapping("/addLocalGuardianDetails")
    public ResponseEntity<?> addLocalGuardianDetails(
            @RequestParam String formNumber,
            @RequestBody LocalGuardianDetails localGuardianDetails) {

        try {
            HostelForm hostelForm = hostelFormService.getHostelFormByFormNumber(formNumber);
            LocalGuardianDetails savedGuardianDetails = localGuardianDetailsService.saveLocalGuardianDetails(localGuardianDetails, formNumber);
            hostelForm.setLocalGuardianDetails(savedGuardianDetails);
            hostelFormService.saveHostelForm(hostelForm);
            return ResponseEntity.ok(hostelForm);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "HostelForm not found with formNumber: " + formNumber));
        }
    }


    @PutMapping("/updateLocalGuardianDetails/{guardianId}")
    public ResponseEntity<?> updateLocalGuardianDetails(
            @PathVariable Long guardianId,
            @RequestBody LocalGuardianDetails localGuardianDetails) {
        try {
            LocalGuardianDetails updatedDetails = localGuardianDetailsService.updateLocalGuardianDetails(guardianId, localGuardianDetails);
            return ResponseEntity.ok(updatedDetails);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error updating Local Guardian Details"));
        }
    }

    @GetMapping("/getLocalGuardianDetailsById/{guardianId}")
    public ResponseEntity<?> getLocalGuardianDetailsById(@PathVariable Long guardianId) {
        try {
            LocalGuardianDetails details = localGuardianDetailsService.getLocalGuardianDetailsById(guardianId);
            return ResponseEntity.ok(details);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Local Guardian Details not found with ID: " + guardianId));
        }
    }

    @GetMapping("/getAllLocalGuardianDetails")
    public ResponseEntity<?> getAllLocalGuardianDetails() {
        try {
            List<LocalGuardianDetails> detailsList = localGuardianDetailsService.getAllLocalGuardianDetails();
            return ResponseEntity.ok(detailsList);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error retrieving Local Guardian Details"));
        }
    }

    @DeleteMapping("/deleteLocalGuardianDetails/{guardianId}")
    public ResponseEntity<?> deleteLocalGuardianDetails(@PathVariable Long guardianId) {
        try {
            localGuardianDetailsService.deleteLocalGuardianDetails(guardianId);
            return ResponseEntity.ok(Map.of("message", "Local Guardian Details deleted successfully."));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error deleting Local Guardian Details with ID: " + guardianId));
        }
    }
}
