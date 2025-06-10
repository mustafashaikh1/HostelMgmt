package com.Hostel.Controller;

import com.Hostel.Entity.Bed;
import com.Hostel.Entity.OfficeUseOnly;
import com.Hostel.Entity.Payment;
import com.Hostel.Repository.BedRepository;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Repository.PaymentRepository;
import com.Hostel.Service.OfficeUseOnlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
//@CrossOrigin(origins = "https://pjsofttech.in")
public class OfficeUseOnlyController {

    @Autowired
    private OfficeUseOnlyService officeUseOnlyService;

    @Autowired
    private HostelFormRepository hostelFormRepository;


    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BedRepository bedRepository;

    @PostMapping("/saveOfficeUseOnly")
    public ResponseEntity<Map<String, Object>> saveOfficeUseOnly(
            @RequestBody OfficeUseOnly officeUseOnly,
            @RequestParam String formNumber) {

        OfficeUseOnly savedOfficeUseOnly = officeUseOnlyService.saveOfficeUseOnly(officeUseOnly, formNumber);

        // Fetch updated bed information
        Optional<Bed> updatedBed = bedRepository.findByBedNumber(savedOfficeUseOnly.getBedNumber());

        Map<String, Object> response = new HashMap<>();
        response.put("officeUseOnlyId", savedOfficeUseOnly.getOfficeUseOnlyId());
        response.put("totalFees", savedOfficeUseOnly.getTotalFees());
        response.put("depositCollected", savedOfficeUseOnly.getDepositCollected());
        response.put("remainingFees", savedOfficeUseOnly.getRemainingFees());
        response.put("gstIncluded", savedOfficeUseOnly.isGstIncluded());
        response.put("gstAmount", savedOfficeUseOnly.getGstAmount());
        response.put("gstNumber", savedOfficeUseOnly.getGstNumber());
        response.put("studentName", savedOfficeUseOnly.getStudentName());
        response.put("formNumber", formNumber);
        response.put("guardianName", savedOfficeUseOnly.getGuardianName());
        response.put("contactNumber", savedOfficeUseOnly.getContactNumber());
        response.put("admissionDate", savedOfficeUseOnly.getAdmissionDate());
        response.put("isActive", savedOfficeUseOnly.isActive());

        // Include the bed allocation status
        if (updatedBed.isPresent()) {
            response.put("bedNumber", updatedBed.get().getBedNumber());
            response.put("allocated", updatedBed.get().isAllocated()); // Ensure it returns true
        } else {
            response.put("bedNumber", "Not Found");
            response.put("allocated", false);
        }

        return ResponseEntity.ok(response);
    }


    @PutMapping("/updateOfficeUseOnly/{id}")
    public ResponseEntity<OfficeUseOnly> updateOfficeUseOnly(@PathVariable Long id, @RequestBody OfficeUseOnly officeUseOnly) {
        return ResponseEntity.ok(officeUseOnlyService.updateOfficeUseOnly(id, officeUseOnly));
    }

    @PostMapping("/addPayment/{formNumber}")
    public ResponseEntity<Map<String, Object>> addPayment(@PathVariable String formNumber, @RequestBody Payment payment) {
        return ResponseEntity.ok(officeUseOnlyService.addPayment(formNumber, payment));
    }


    @GetMapping("/getAllOfficeUseOnly")
    public ResponseEntity<List<OfficeUseOnly>> getAllOfficeUseOnly() {
        return ResponseEntity.ok(officeUseOnlyService.getAllOfficeUseOnly());
    }

    @GetMapping("/getOfficeUseOnlyById/{id}")
    public ResponseEntity<OfficeUseOnly> getOfficeUseOnlyById(@PathVariable Long id) {
        OfficeUseOnly officeUseOnly = officeUseOnlyService.getOfficeUseOnlyById(id);
        return ResponseEntity.ok(officeUseOnly);
    }


    @GetMapping("/getByFormNumber")
    public ResponseEntity<OfficeUseOnly> getByFormNumber(@RequestParam String formNumber) {
        return ResponseEntity.ok(officeUseOnlyService.getOfficeUseOnlyByFormNumber(formNumber));
    }

    @DeleteMapping("/deleteOfficeUseOnly/{id}")
    public ResponseEntity<String> deleteOfficeUseOnly(@PathVariable Long id) {
        officeUseOnlyService.deleteOfficeUseOnly(id);
        return ResponseEntity.ok("OfficeUseOnly record deleted successfully!");
    }

    @GetMapping("/stats")
    public Map<String, Object> getDashboardStats() {
        return officeUseOnlyService.getDashboardStats();
    }





    @GetMapping("/getPayments/{formNumber}")
    public List<Payment> getPaymentsByFormNumber(String formNumber) {
        // Check if formNumber exists in HostelForm
        if (!hostelFormRepository.existsByFormNumber(formNumber)) {
            throw new RuntimeException("HostelForm not found for form number: " + formNumber);
        }

        // Retrieve payments from the repository
        return paymentRepository.findByFormNumber(formNumber);
    }

}