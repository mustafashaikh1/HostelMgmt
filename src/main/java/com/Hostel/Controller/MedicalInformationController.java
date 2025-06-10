package com.Hostel.Controller;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.MedicalInformation;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Service.HostelFormService;
import com.Hostel.Service.MedicalInformationService;
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
public class MedicalInformationController {

    @Autowired
    private MedicalInformationService medicalInformationService;

    @Autowired
    private HostelFormService hostelFormService;


    @Autowired
    private HostelFormRepository hostelFormRepository;



    @PostMapping("/createMedicalInformation")
    public ResponseEntity<?> createMedicalInformation(
            @RequestParam String formNumber,
            @RequestBody MedicalInformation medicalInformation) {

        try {
            HostelForm hostelForm = hostelFormService.getHostelFormByFormNumber(formNumber);
            MedicalInformation savedMedicalInfo = medicalInformationService.saveMedicalInformation(medicalInformation, formNumber);
            hostelForm.setMedicalInformation(savedMedicalInfo);
            hostelFormService.saveHostelForm(hostelForm);
            return ResponseEntity.ok(hostelForm);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "HostelForm not found with formNumber: " + formNumber));
        }
    }





    // Endpoint to update MedicalInformation by ID
    @PutMapping("/updateMedicalInfo/{id}")
    public ResponseEntity<MedicalInformation> updateMedicalInformation(
            @PathVariable("id") Long medicalInfoId,
            @RequestBody MedicalInformation medicalInformation) {
        MedicalInformation updatedMedicalInfo = medicalInformationService.updateMedicalInformation(medicalInfoId, medicalInformation);
        return ResponseEntity.ok(updatedMedicalInfo);
    }

    // Endpoint to get MedicalInformation by ID
    @GetMapping("/getMedicalInformationById/{id}")
    public ResponseEntity<MedicalInformation> getMedicalInformationById(@PathVariable("id") Long medicalInfoId) {
        MedicalInformation medicalInformation = medicalInformationService.getMedicalInformationById(medicalInfoId);
        return ResponseEntity.ok(medicalInformation);
    }

    // Endpoint to get all MedicalInformation records
    @GetMapping("/getAllMedicalInformation")
    public ResponseEntity<List<MedicalInformation>> getAllMedicalInformation() {
        List<MedicalInformation> allMedicalInfo = medicalInformationService.getAllMedicalInformation();
        return ResponseEntity.ok(allMedicalInfo);
    }

    // Endpoint to delete MedicalInformation by ID
    @DeleteMapping("/deleteMedicalInformation/{id}")
    public ResponseEntity<Void> deleteMedicalInformation(@PathVariable("id") Long medicalInfoId) {
        medicalInformationService.deleteMedicalInformation(medicalInfoId);
        return ResponseEntity.noContent().build();
    }
}
