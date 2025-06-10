package com.Hostel.Controller;


import com.Hostel.Entity.AdmissionForm;
import com.Hostel.Entity.Bed;
import com.Hostel.Repository.BedRepository;
import com.Hostel.Service.AdmissionFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admissionForms")
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class AdmissionFormController {

    @Autowired
    private AdmissionFormService admissionFormService;

    @Autowired
    private BedRepository bedRepository;

    // Save a new Admission Form

    @PostMapping("/saveAdmissionForm")
    public ResponseEntity<Map<String, Object>> saveAdmissionForm(
            @RequestParam("signatureFile") MultipartFile signatureFile,
            @RequestParam("formNumber") String formNumber,
            @RequestParam("studentName") String studentName,
            @RequestParam("email") String email,
            @RequestParam("mobileNo") String mobileNo,
            @RequestParam("roomNumber") String roomNumber,
            @RequestParam("roomType") String roomType,
            @RequestParam("bedType") String bedType,
            @RequestParam("bedNumber") String bedNumber,
            @RequestParam("floor") String floor,
            @RequestParam("monthName") String monthName,
            @RequestParam("monthRent") Double monthRent,
            @RequestParam("paymentMode") String paymentMode,
            @RequestParam("year") String year,
            @RequestParam("admissionDate") String admissionDate,
            @RequestParam("conductedBy") String conductedBy,
            @RequestParam("source") String source,
            @RequestParam("paymentStatus") String paymentStatus,
            @RequestParam("transactionNumber") String transactionNumber,
            @RequestParam("gstIncluded") Boolean gstIncluded,
            @RequestParam(value = "gstPercentage", required = false) Double gstPercentage,
            @RequestParam(value = "gstNumber", required = false) String gstNumber,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestParam("numberOfMonths") Integer numberOfMonths) {

        try {
            AdmissionForm savedAdmissionForm = admissionFormService.saveAdmissionForm(
                    new AdmissionForm(), signatureFile, formNumber, studentName, email, mobileNo, roomNumber,
                    roomType, bedType, bedNumber, floor, monthName, monthRent, paymentMode, year,
                    admissionDate, conductedBy, source, paymentStatus, transactionNumber, gstIncluded,
                    gstPercentage, gstNumber, fromDate, toDate, numberOfMonths
            );

            // Fetch updated bed information
            Optional<Bed> updatedBed = bedRepository.findByBedNumber(savedAdmissionForm.getBedNumber());

            Map<String, Object> response = new HashMap<>();
            response.put("admissionId", savedAdmissionForm.getAdmissionId());
            response.put("studentName", savedAdmissionForm.getStudentName());
            response.put("email", savedAdmissionForm.getEmail());
            response.put("mobileNo", savedAdmissionForm.getMobileNo());
            response.put("formNumber", savedAdmissionForm.getFormNumber());
            response.put("roomNumber", savedAdmissionForm.getRoomNumber());
            response.put("roomType", savedAdmissionForm.getRoomType());
            response.put("bedType", savedAdmissionForm.getBedType());
            response.put("bedNumber", savedAdmissionForm.getBedNumber());
            response.put("floor", savedAdmissionForm.getFloor());
            response.put("monthName", savedAdmissionForm.getMonthName());
            response.put("monthRent", savedAdmissionForm.getMonthRent());
            response.put("paymentMode", savedAdmissionForm.getPaymentMode());
            response.put("year", savedAdmissionForm.getYear());
            response.put("admissionDate", savedAdmissionForm.getAdmissionDate());
            response.put("conductedBy", savedAdmissionForm.getConductedBy());
            response.put("source", savedAdmissionForm.getSource());
            response.put("paymentStatus", savedAdmissionForm.getPaymentStatus());
            response.put("transactionNumber", savedAdmissionForm.getTransactionNumber());
            response.put("gstIncluded", savedAdmissionForm.getGstIncluded());
            response.put("gstPercentage", savedAdmissionForm.getGstPercentage());
            response.put("gstNumber", savedAdmissionForm.getGstNumber());
            response.put("gstAmount", savedAdmissionForm.getGstAmount());
            response.put("totalAmount", savedAdmissionForm.getTotalAmount());
            response.put("fromDate", savedAdmissionForm.getFromDate());
            response.put("toDate", savedAdmissionForm.getToDate());
            response.put("numberOfMonths", savedAdmissionForm.getNumberOfMonths());
            response.put("totalRent", savedAdmissionForm.getTotalRent());
            response.put("isActive", savedAdmissionForm.getIsActive()); // This will always be true now
            response.put("bedAllocationStatus", savedAdmissionForm.getBedAllocationStatus());
            response.put("authorizedSignatureUrl", savedAdmissionForm.getAuthorizedSignatureUrl());

            // Include the bed allocation status
            if (updatedBed.isPresent()) {
                response.put("bedAllocated", updatedBed.get().isAllocated()); // true/false
            } else {
                response.put("bedAllocated", false);
            }

            response.put("message", "Admission form saved successfully");
            response.put("success", true);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IOException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error saving admission form: " + e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Unexpected error occurred: " + e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PutMapping("/updateAdmissionForm/{id}")
    public ResponseEntity<AdmissionForm> updateAdmissionForm(@PathVariable("id") Long id,
                                                             @RequestParam("signatureFile") MultipartFile signatureFile,
                                                             @RequestParam("formNumber") String formNumber,
                                                             @RequestParam("studentName") String studentName,
                                                             @RequestParam("email") String email,
                                                             @RequestParam("mobileNo") String mobileNo,
                                                             @RequestParam("roomNumber") String roomNumber,
                                                             @RequestParam("roomType") String roomType,
                                                             @RequestParam("bedType") String bedType,
                                                             @RequestParam("bedNumber") String bedNumber,
                                                             @RequestParam("floor") String floor,
                                                             @RequestParam("monthName") String monthName,
                                                             @RequestParam("monthRent") Double monthRent,
                                                             @RequestParam("paymentMode") String paymentMode,
                                                             @RequestParam("year") String year,
                                                             @RequestParam("admissionDate") String admissionDate,
                                                             @RequestParam("conductedBy") String conductedBy,
                                                             @RequestParam("source") String source,
                                                             @RequestParam("paymentStatus") String paymentStatus,
                                                             @RequestParam(value = "transactionNumber", required = false) String transactionNumber,
                                                             @RequestParam(value = "gstIncluded", required = false, defaultValue = "false") boolean gstIncluded,
                                                             @RequestParam(value = "gstPercentage", required = false) Double gstPercentage,
                                                             @RequestParam(value = "gstNumber", required = false) String gstNumber,
                                                             @RequestParam("fromDate") String fromDate,
                                                             @RequestParam("toDate") String toDate,
                                                             @RequestParam("numberOfMonths") Integer numberOfMonths) throws IOException {

        AdmissionForm admissionForm = new AdmissionForm();
        admissionForm.setFormNumber(formNumber);
        admissionForm.setStudentName(studentName);
        admissionForm.setEmail(email);
        admissionForm.setMobileNo(mobileNo);
        admissionForm.setRoomNumber(roomNumber);
        admissionForm.setRoomType(roomType);
        admissionForm.setBedType(bedType);
        admissionForm.setBedNumber(bedNumber);
        admissionForm.setFloor(floor);
        admissionForm.setMonthName(monthName);
        admissionForm.setMonthRent(monthRent);
        admissionForm.setPaymentMode(paymentMode);
        admissionForm.setYear(year);
        admissionForm.setAdmissionDate(LocalDate.parse(admissionDate));
        admissionForm.setConductedBy(conductedBy);
        admissionForm.setSource(source);
        admissionForm.setPaymentStatus(paymentStatus);
        admissionForm.setFromDate(LocalDate.parse(fromDate));
        admissionForm.setToDate(LocalDate.parse(toDate));
        admissionForm.setNumberOfMonths(Double.valueOf(numberOfMonths));


        // New GST and transaction fields
        admissionForm.setTransactionNumber(transactionNumber);
        admissionForm.setGstIncluded(gstIncluded);
        admissionForm.setGstNumber(gstNumber);

        if (gstIncluded != false && gstPercentage != null) {
            admissionForm.setGstPercentage(gstPercentage);
            double gstAmount = (monthRent * gstPercentage) / 100;
            double totalAmount = monthRent + gstAmount;
            admissionForm.setGstAmount(gstAmount);
            admissionForm.setTotalAmount(totalAmount);
        } else {
            admissionForm.setTotalAmount(monthRent);
            admissionForm.setGstPercentage(0.0);
            admissionForm.setGstAmount(0.0);
        }

        AdmissionForm updatedForm = admissionFormService.updateAdmissionForm(id, admissionForm, signatureFile);
        return updatedForm != null ? new ResponseEntity<>(updatedForm, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    // Get all Admission Forms
    @GetMapping("/getAllAdmissionForms")
    public ResponseEntity<List<AdmissionForm>> getAllAdmissionForms() {
        List<AdmissionForm> admissionForms = admissionFormService.getAllAdmissionForms();
        return new ResponseEntity<>(admissionForms, HttpStatus.OK);
    }

    // Get an Admission Form by ID
    @GetMapping("/getAdmissionFormById/{id}")
    public ResponseEntity<AdmissionForm> getAdmissionFormById(@PathVariable("id") Long id) {
        Optional<AdmissionForm> admissionForm = admissionFormService.getAdmissionFormById(id);
        return admissionForm.map(form -> new ResponseEntity<>(form, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get an Admission Form by Form Number
    @GetMapping("/getAdmissionFormByFormNumber/formNumber/{formNumber}")
    public ResponseEntity<List<AdmissionForm>> getAdmissionFormByFormNumber(@PathVariable("formNumber") String formNumber) {
        List<AdmissionForm> forms = admissionFormService.getAdmissionFormByFormNumber(formNumber);
        if (forms.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(forms, HttpStatus.OK);
    }



    // Delete an Admission Form
    @DeleteMapping("/deleteAdmissionForm/{id}")
    public ResponseEntity<Void> deleteAdmissionForm(@PathVariable("id") Long id) {
        admissionFormService.deleteAdmissionForm(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content status when deleted successfully
    }


    @GetMapping("/getDateWiseAdmissionStats")
    public ResponseEntity<Map<String, Object>> getDateWiseAdmissionStats() {
        Map<String, Object> stats = admissionFormService.getDateWiseAdmissionStats();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }


    @GetMapping("/admissionSourceWise")
    public ResponseEntity<Map<String, Long>> getSourceWiseGraph(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, Long> result = admissionFormService.getSourceWiseAdmissions(startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAdmissionRevenueStats")
    public ResponseEntity<Map<String, Object>> getRevenueStats() {
        Map<String, Object> stats = admissionFormService.getRevenueStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/monthWiseAdmissionStats")
    public ResponseEntity<List<Map<String, Object>>> getMonthWiseAdmissionStats(@RequestParam("year") Integer year) {
        try {
            List<Map<String, Object>> stats = admissionFormService.getMonthWiseAdmissionStatsByYear(year);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/getYearWiseAdmissionStatsByYear")
    public ResponseEntity<Map<String, Object>> getYearWiseAdmissionStatsByYear(@RequestParam int year) {
        Map<String, Object> stats = admissionFormService.getYearWiseAdmissionStatsByYear(year);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }


    @GetMapping("getDateWiseSummaryWithMonth")
    public List<Map<String, Object>> getDateWiseSummaryWithMonth(@RequestParam String monthName,
                                                                 @RequestParam String year) {
        return admissionFormService.getDateWiseSummaryWithMonth(monthName, year);
    }

}
