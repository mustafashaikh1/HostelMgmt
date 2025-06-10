package com.Hostel.ServiceImpl;

import com.Hostel.Entity.Bed;
import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.OfficeUseOnly;
import com.Hostel.Entity.Payment;
import com.Hostel.Repository.*;
import com.Hostel.Service.OfficeUseOnlyService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OfficeUseOnlyServiceImpl implements OfficeUseOnlyService {

    @Autowired
    private HostelInquiryRepository hostelInquiryRepository;

    @Autowired
    private OfficeUseOnlyRepository officeUseOnlyRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private HostelFormRepository hostelFormRepository;

    @Autowired
    private BedRepository bedRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


//    @Override
//    public OfficeUseOnly saveOfficeUseOnly(OfficeUseOnly officeUseOnly,String formNumber) {
//
//
//        HostelForm hostelForm = hostelFormRepository.findByFormNumber(formNumber)
//                .orElseThrow(() -> new RuntimeException("HostelForm not found with formNumber: " + formNumber));
//
//        // Initialize lazy-loaded fields manually to avoid lazy loading issues
//        Hibernate.initialize(hostelForm.getBeds());
//        Hibernate.initialize(hostelForm.getAdmin());
//
//        // Set the HostelForm to OfficeUseOnly
//        hostelForm.setOfficeUseOnly(officeUseOnly);
//
//
//        // Save OfficeUseOnly
//        OfficeUseOnly savedOfficeUseOnly = officeUseOnlyRepository.save(officeUseOnly);
//
//        // Save updated HostelForm
//        hostelFormRepository.save(hostelForm);
//
//
//        System.out.println("Before Save - isActive: " + officeUseOnly.isActive());
//
//        officeUseOnly.calculateFees(); // Calculate remaining fees before saving
//        officeUseOnly = officeUseOnlyRepository.save(officeUseOnly);
//
//        System.out.println("After Save - isActive: " + savedOfficeUseOnly.isActive());
//        // Save the updated HostelForm
//
//
//        return savedOfficeUseOnly;
//    }

    @Override
    public OfficeUseOnly saveOfficeUseOnly(OfficeUseOnly officeUseOnly, String formNumber) {

        HostelForm hostelForm = hostelFormRepository.findByFormNumber(formNumber)
                .orElseThrow(() -> new RuntimeException("HostelForm not found with formNumber: " + formNumber));

        Hibernate.initialize(hostelForm.getBeds());
        Hibernate.initialize(hostelForm.getAdmin());

        // Associate OfficeUseOnly with HostelForm
        hostelForm.setOfficeUseOnly(officeUseOnly);

        // Fetch the corresponding Bed using bedNumber
        OfficeUseOnly finalOfficeUseOnly = officeUseOnly;
        Bed bed = bedRepository.findByBedNumber(officeUseOnly.getBedNumber())
                .orElseThrow(() -> new RuntimeException("Bed not found with bedNumber: " + finalOfficeUseOnly.getBedNumber()));

        // Mark the bed as allocated
        bed.setAllocated(true);
        bed.setHostelForm(hostelForm); // Associate with the HostelForm if needed
        bedRepository.save(bed); // Save the updated Bed

        // Save OfficeUseOnly
        OfficeUseOnly savedOfficeUseOnly = officeUseOnlyRepository.save(officeUseOnly);

        // Save updated HostelForm
        hostelFormRepository.save(hostelForm);

        officeUseOnly.calculateFees();
        officeUseOnly = officeUseOnlyRepository.save(officeUseOnly);

        return savedOfficeUseOnly;
    }


    @Override
    public OfficeUseOnly updateOfficeUseOnly(Long id, OfficeUseOnly officeUseOnly) {
        Optional<OfficeUseOnly> existing = officeUseOnlyRepository.findById(id);
        if (existing.isPresent()) {
            OfficeUseOnly updated = existing.get();

            // Update the fields from the input object (officeUseOnly)
            updated.setFloor(officeUseOnly.getFloor());
            updated.setRoomNumber(officeUseOnly.getRoomNumber());
            updated.setBedType(officeUseOnly.getBedType());
            updated.setRoomType(officeUseOnly.getRoomType());
            updated.setStudentName(officeUseOnly.getStudentName());
            updated.setGuardianName(officeUseOnly.getGuardianName());
            updated.setContactNumber(officeUseOnly.getContactNumber());
            updated.setAdmissionDate(officeUseOnly.getAdmissionDate());
            updated.setRemark(officeUseOnly.getRemark());
            updated.setBedNumber(officeUseOnly.getBedNumber());
            updated.setActive(officeUseOnly.isActive());

            // Update GST details if they exist
            updated.setGstIncluded(officeUseOnly.isGstIncluded());
            updated.setGstAmount(officeUseOnly.getGstAmount());
            updated.setGstNumber(officeUseOnly.getGstNumber());

            // Update the status field if provided
            if (officeUseOnly.getStatus() != null && !officeUseOnly.getStatus().isEmpty()) {
                updated.setStatus(officeUseOnly.getStatus());
            }

            // Recalculate fees based on updated data
            updated.calculateFees();

            // Save and return the updated OfficeUseOnly object
            return officeUseOnlyRepository.save(updated);
        } else {
            throw new RuntimeException("OfficeUseOnly not found with id: " + id);
        }
    }


    @Override
    public Map<String, Object> addPayment(String formNumber, Payment payment) {
        // Step 1: Check if the formNumber exists in HostelForm
        HostelForm hostelForm = hostelFormRepository.findByFormNumber(formNumber)
                .orElseThrow(() -> new RuntimeException("HostelForm not found for form number: " + formNumber));

        // Step 2: Retrieve the OfficeUseOnly record associated with the formNumber
        OfficeUseOnly officeUseOnly = officeUseOnlyRepository.findByFormNumber(formNumber)
                .orElseThrow(() -> new RuntimeException("OfficeUseOnly record not found for form number: " + formNumber));

        // Step 3: Save payment details
        payment.setFormNumber(formNumber);
        payment.setPaymentDate(LocalDate.now()); // Automatically set today's date
        payment.setOfficeUseOnly(officeUseOnly);
        payment = paymentRepository.save(payment);

        // Step 4: Update remaining fees after payment
        officeUseOnly.calculateFees();
        officeUseOnlyRepository.save(officeUseOnly);

        // Step 5: Retrieve all payments related to this form number
        List<Map<String, Object>> paymentsList = paymentRepository.findByFormNumber(formNumber).stream().map(p -> {
            Map<String, Object> paymentMap = new HashMap<>();
            paymentMap.put("paymentId", p.getPaymentId());
            paymentMap.put("amount", p.getAmount());
            paymentMap.put("paymentMode", p.getPaymentMode());
            paymentMap.put("transactionNumber", p.getTransactionNumber());
            paymentMap.put("paymentDate", p.getPaymentDate().toString());
            paymentMap.put("status", p.getStatus());
            paymentMap.put("conductedBy", officeUseOnly.getConductedBy());
            return paymentMap;
        }).toList();

        // Step 6: Prepare structured response
        Map<String, Object> response = new HashMap<>();
        response.put("officeUseOnlyId", officeUseOnly.getOfficeUseOnlyId());

        Map<String, Object> studentDetails = new HashMap<>();
        studentDetails.put("studentName", officeUseOnly.getStudentName());
        studentDetails.put("guardianName", officeUseOnly.getGuardianName());
        studentDetails.put("contactNumber", officeUseOnly.getContactNumber());
        studentDetails.put("admissionDate", officeUseOnly.getAdmissionDate());
        response.put("studentDetails", studentDetails);

        Map<String, Object> roomDetails = new HashMap<>();
        roomDetails.put("floor", officeUseOnly.getFloor());
        roomDetails.put("roomNumber", officeUseOnly.getRoomNumber());
        roomDetails.put("bedType", officeUseOnly.getBedType());
        roomDetails.put("bedNumber", officeUseOnly.getBedNumber());
        roomDetails.put("roomType", officeUseOnly.getRoomType());
        response.put("roomDetails", roomDetails);

        Map<String, Object> feesDetails = new HashMap<>();
        feesDetails.put("totalFees", officeUseOnly.getTotalFees());
        feesDetails.put("depositCollected", officeUseOnly.getDepositCollected());
        feesDetails.put("gstIncluded", officeUseOnly.isGstIncluded());
        feesDetails.put("gstAmount", officeUseOnly.getGstAmount());
        feesDetails.put("gstNumber", officeUseOnly.getGstNumber());

        feesDetails.put("remainingFees", officeUseOnly.getRemainingFees());
        feesDetails.put("remark", officeUseOnly.getRemark());
        response.put("feesDetails", feesDetails);

        response.put("isActive", officeUseOnly.isActive());
        response.put("payments", paymentsList);

        return response;
    }



    @Override
    public List<OfficeUseOnly> getAllOfficeUseOnly() {
        return officeUseOnlyRepository.findAll();
    }

    @Override
    public OfficeUseOnly getOfficeUseOnlyById(Long id) {
        return officeUseOnlyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OfficeUseOnly not found"));
    }

    @Override
    public OfficeUseOnly getOfficeUseOnlyByFormNumber(String formNumber) {
        return officeUseOnlyRepository.findByFormNumber(formNumber)
                .orElseThrow(() -> new RuntimeException("Office data not found"));
    }

    @Override
    public void deleteOfficeUseOnly(Long id) {
        officeUseOnlyRepository.deleteById(id);
    }

    @Override
    public Map<String, Object> getDashboardStats() {
        LocalDate today = LocalDate.now();
        LocalDate last7Days = today.minusDays(7);
        LocalDate last30Days = today.minusDays(30);
        LocalDate last365Days = today.minusDays(365);

        // Inquiry Counts
        long inquiriesToday = hostelInquiryRepository.countInquiriesByDate(today);
        long inquiriesLast7Days = hostelInquiryRepository.countInquiriesBetweenDates(last7Days, today);
        long inquiriesLast30Days = hostelInquiryRepository.countInquiriesBetweenDates(last30Days, today);
        long inquiriesLast365Days = hostelInquiryRepository.countInquiriesBetweenDates(last365Days, today);
        long totalInquiries = hostelInquiryRepository.count();

        // Revenue Calculations
        double revenueToday = officeUseOnlyRepository.sumRevenueByDate(today.format(formatter)) != null ? officeUseOnlyRepository.sumRevenueByDate(today.format(formatter)) : 0.0;
        double revenueLast7Days = officeUseOnlyRepository.sumRevenueBetweenDates(last7Days.format(formatter), today.format(formatter)) != null ? officeUseOnlyRepository.sumRevenueBetweenDates(last7Days.format(formatter), today.format(formatter)) : 0.0;
        double revenueLast30Days = officeUseOnlyRepository.sumRevenueBetweenDates(last30Days.format(formatter), today.format(formatter)) != null ? officeUseOnlyRepository.sumRevenueBetweenDates(last30Days.format(formatter), today.format(formatter)) : 0.0;
        double revenueLast365Days = officeUseOnlyRepository.sumRevenueBetweenDates(last365Days.format(formatter), today.format(formatter)) != null ? officeUseOnlyRepository.sumRevenueBetweenDates(last365Days.format(formatter), today.format(formatter)) : 0.0;
        double totalRevenue = officeUseOnlyRepository.sumRevenueBetweenDates("2000-01-01", today.format(formatter)) != null ? officeUseOnlyRepository.sumRevenueBetweenDates("2000-01-01", today.format(formatter)) : 0.0;

        Map<String, Object> response = new HashMap<>();

        response.put("today", Map.of("count", inquiriesToday, "revenue", revenueToday));
        response.put("last7Days", Map.of("count", inquiriesLast7Days, "revenue", revenueLast7Days));
        response.put("last30Days", Map.of("count", inquiriesLast30Days, "revenue", revenueLast30Days));
        response.put("last365Days", Map.of("count", inquiriesLast365Days, "revenue", revenueLast365Days));
        response.put("total", Map.of("count", totalInquiries, "revenue", totalRevenue));

        return response;
    }







    @Override
    public List<Payment> getPaymentsByFormNumber(String formNumber) {
        return paymentRepository.findByFormNumber(formNumber);
    }


}