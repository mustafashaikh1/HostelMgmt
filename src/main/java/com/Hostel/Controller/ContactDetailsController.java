package com.Hostel.Controller;

import com.Hostel.Entity.ContactDetails;
import com.Hostel.Entity.HostelForm;
import com.Hostel.Service.ContactDetailsService;
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
public class ContactDetailsController {

    @Autowired
    private ContactDetailsService contactDetailsService;

    @Autowired
    private HostelFormService hostelFormService;

    @PostMapping("/addContactDetails")
    public ResponseEntity<?> addContactDetails(
            @RequestParam String formNumber,
            @RequestBody ContactDetails contactDetails) {

        try {
            HostelForm hostelForm = hostelFormService.getHostelFormByFormNumber(formNumber);
            ContactDetails savedContactDetails = contactDetailsService.saveContactDetails(contactDetails, formNumber);
            hostelForm.setContactDetails(savedContactDetails);
            hostelFormService.saveHostelForm(hostelForm);
            return ResponseEntity.ok(hostelForm);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "HostelForm not found with formNumber: " + formNumber));
        }
    }

    @GetMapping("/getAllContactDetails")
    public ResponseEntity<?> getAllContactDetails() {
        try {
            List<ContactDetails> contactDetailsList = contactDetailsService.getAllContactDetails();
            return ResponseEntity.ok(contactDetailsList);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        }
    }

    @GetMapping("/getContactDetailsById/{contactId}")
    public ResponseEntity<?> getContactDetailsById(@PathVariable Long contactId) {
        try {
            ContactDetails contactDetails = contactDetailsService.getContactDetailsById(contactId);
            return ResponseEntity.ok(contactDetails);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        }
    }

    @PutMapping("/updateContactDetails/{contactId}")
    public ResponseEntity<?> updateContactDetails(
            @PathVariable Long contactId,
            @RequestBody ContactDetails contactDetails) {
        try {
            ContactDetails updatedContactDetails = contactDetailsService.updateContactDetails(contactId, contactDetails);
            return ResponseEntity.ok(updatedContactDetails);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        }
    }

    @DeleteMapping("/deleteContactDetails/{contactId}")
    public ResponseEntity<?> deleteContactDetails(@PathVariable Long contactId) {
        try {
            contactDetailsService.deleteContactDetails(contactId);
            return ResponseEntity.ok(Map.of("message", "ContactDetails with ID " + contactId + " has been deleted."));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        }
    }
}
