package com.Hostel.Controller;

import com.Hostel.Entity.UndertakingForm;
import com.Hostel.Service.UndertakingFormService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UndertakingFormController {

    @Autowired
    private UndertakingFormService undertakingFormService;

    // Save UndertakingForm and associate it with a HostelForm (formNumber)
    @PostMapping("/saveUndertakingForm")
    public ResponseEntity<UndertakingForm> saveUndertakingForm(
            @RequestParam(value = "otherProofImage", required = false) MultipartFile otherProofImage,
            @RequestParam(value = "parentOtherProofImage", required = false) MultipartFile parentOtherProofImage,
            @RequestParam("educationDocumentsImage") MultipartFile educationDocumentsImage,
            @RequestParam("applicantSignatureImage") MultipartFile applicantSignatureImage,
            @RequestParam(value = "parentSignatureImage", required = false) MultipartFile parentSignatureImage,
            @RequestParam("formNumber") String formNumber,
            @RequestParam(value = "otherProofType", required = false) String otherProofType,
            @RequestParam(value = "parentOtherProofType", required = false) String parentOtherProofType,
            @RequestParam("educationDocumentsType") String educationDocumentsType,
            @RequestParam("stayFrom") String stayFrom,
            @RequestParam("stayTo") String stayTo) throws IOException {

        UndertakingForm savedForm = undertakingFormService.saveUndertakingForm(
                new UndertakingForm(),
                otherProofImage,
                parentOtherProofImage,
                educationDocumentsImage,
                applicantSignatureImage,
                parentSignatureImage,
                formNumber,
                otherProofType,
                parentOtherProofType,
                educationDocumentsType,
                stayFrom,
                stayTo
        );
        return ResponseEntity.ok(savedForm);
    }

    // Get all UndertakingForms
    @GetMapping("/getAllUndertakingForms")
    public List<UndertakingForm> getAllUndertakingForms() {
        return undertakingFormService.getAllUndertakingForms();
    }

    // Get UndertakingForm by ID
    @GetMapping("/getUndertakingFormById/{id}")
    public ResponseEntity<UndertakingForm> getUndertakingFormById(@PathVariable Long id) {
        UndertakingForm undertakingForm = undertakingFormService.getUndertakingFormById(id);
        return ResponseEntity.ok(undertakingForm);
    }

    // Get UndertakingForm by formNumber
    @GetMapping("/getUndertakingFormByFormNumber/{formNumber}")
    public ResponseEntity<UndertakingForm> getUndertakingFormByFormNumber(@PathVariable String formNumber) {
        UndertakingForm undertakingForm = undertakingFormService.getUndertakingFormByFormNumber(formNumber);
        return ResponseEntity.ok(undertakingForm);
    }

    // Update UndertakingForm by ID
    @PutMapping("/updateUndertakingForm/{id}")
    public ResponseEntity<UndertakingForm> updateUndertakingForm(
            @PathVariable Long id,
            @RequestParam(value = "otherProofImage", required = false) MultipartFile otherProofImage,
            @RequestParam(value = "parentOtherProofImage", required = false) MultipartFile parentOtherProofImage,
            @RequestParam(value = "educationDocumentsImage", required = false) MultipartFile educationDocumentsImage,
            @RequestParam(value = "applicantSignatureImage", required = false) MultipartFile applicantSignatureImage,
            @RequestParam(value = "parentSignatureImage", required = false) MultipartFile parentSignatureImage,
            @RequestParam("formNumber") String formNumber,
            @RequestParam(value = "otherProofType", required = false) String otherProofType,
            @RequestParam(value = "parentOtherProofType", required = false) String parentOtherProofType,
            @RequestParam("educationDocumentsType") String educationDocumentsType,
            @RequestParam("stayFrom") String stayFrom,
            @RequestParam("stayTo") String stayTo) throws IOException {

        UndertakingForm updatedForm = undertakingFormService.updateUndertakingForm(
                id,
                new UndertakingForm(),
                otherProofImage,
                parentOtherProofImage,
                educationDocumentsImage,
                applicantSignatureImage,
                parentSignatureImage,
                formNumber,
                otherProofType,
                parentOtherProofType,
                educationDocumentsType,
                stayFrom,
                stayTo
        );
        return ResponseEntity.ok(updatedForm);
    }

    // Delete UndertakingForm by ID
    @DeleteMapping("/deleteUndertakingForm/{id}")
    public ResponseEntity<Void> deleteUndertakingForm(@PathVariable Long id) {
        undertakingFormService.deleteUndertakingForm(id);
        return ResponseEntity.noContent().build();
    }
}
