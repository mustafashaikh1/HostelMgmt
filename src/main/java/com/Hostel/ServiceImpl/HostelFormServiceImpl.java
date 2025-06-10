package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Repository.*;
import com.Hostel.Service.HostelFormService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Service
public class HostelFormServiceImpl implements HostelFormService {

    @Autowired
    private HostelFormRepository hostelFormRepository;

    @Autowired
    private PersonalInfoRepository personalInfoRepository;

    @Autowired
    private FamilyDetailsRepository familyDetailsRepository;

    @Autowired
    private ContactDetailsRepository contactDetailsRepository;

    @Autowired
    private StudyDetailsRepository studyDetailsRepository;

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private VehicleDetailsRepository vehicleDetailsRepository;

    @Autowired
    private UndertakingFormRepository undertakingFormRepository;

    @Autowired
    private  OfficeUseOnlyRepository officeUseOnlyRepository;

    @Override
    public HostelForm saveHostelForm(HostelForm hostelForm) {
        if (hostelForm.getDate() == null) {
            hostelForm.setDate(LocalDate.now()); // Set the current date if null
        }

        // Check if formNumber is already set (if passed in the request)
        if (hostelForm.getFormNumber() == null || hostelForm.getFormNumber().isEmpty()) {
            // Generate the form number sequentially based on the highest HostelFormId if not set
            Long lastFormNumber = hostelFormRepository.findTopByOrderByHostelFormIdDesc() != null ?
                    hostelFormRepository.findTopByOrderByHostelFormIdDesc().getHostelFormId() : 0L;
            String formNumber = String.valueOf(lastFormNumber + 1); // Increment the last form number
            hostelForm.setFormNumber(formNumber); // Set the sequential form number
        }

        return hostelFormRepository.save(hostelForm);
    }



    @Override
    public List<HostelForm> getAllHostelForms() {
        return hostelFormRepository.findAll();
    }

    @Override
    public HostelForm getHostelFormById(Long HostelFormId) {
        return hostelFormRepository.findById(HostelFormId)
                .orElseThrow(() -> new RuntimeException("HostelForm not found with ID: " + HostelFormId));
    }

    @Override
    public HostelForm getHostelFormByFormNumber(String formNumber) {
        return hostelFormRepository.findByFormNumber(formNumber)
                .orElseThrow(() -> new RuntimeException("HostelForm not found with formNumber: " + formNumber));
    }




    @Override
    public HostelForm updateHostelForm(Long HostelFormId, HostelForm hostelForm) {
        HostelForm existingForm = getHostelFormById(HostelFormId);
        existingForm.setFormNumber(hostelForm.getFormNumber());
        existingForm.setDate(hostelForm.getDate());
        return hostelFormRepository.save(existingForm);
    }

    @Override
    public void deleteHostelForm(Long HostelFormId) {
        hostelFormRepository.deleteById(HostelFormId);
    }




    @Override
    @Transactional
    public void deleteHostelFormByFormNumber(String formNumber) {
        HostelForm hostelForm = hostelFormRepository.findByFormNumber(formNumber)
                .orElseThrow(() -> new RuntimeException("HostelForm not found"));

        // Delete dependent entities first
        if (hostelForm.getPersonalInfo() != null) {
            personalInfoRepository.delete(hostelForm.getPersonalInfo());
        }
        if (hostelForm.getFamilyDetails() != null) {
            familyDetailsRepository.delete(hostelForm.getFamilyDetails());
        }
        if (hostelForm.getContactDetails() != null) {
            contactDetailsRepository.delete(hostelForm.getContactDetails());
        }
        if (hostelForm.getStudyDetails() != null) {
            studyDetailsRepository.delete(hostelForm.getStudyDetails());
        }
        if (hostelForm.getUserActivity() != null) {
            userActivityRepository.delete(hostelForm.getUserActivity());
        }
        if (hostelForm.getVehicleDetails() != null) {
            vehicleDetailsRepository.delete(hostelForm.getVehicleDetails());
        }
        if (hostelForm.getUndertakingForm() != null) {
            undertakingFormRepository.delete(hostelForm.getUndertakingForm());
        }

        // Now delete the HostelForm
        hostelFormRepository.delete(hostelForm);
    }


    @Override
    public Map<String, Object> getAdmissionStats() {
        LocalDate today = LocalDate.now();
        LocalDate last7Days = today.minusDays(7);
        LocalDate last30Days = today.minusDays(30);
        LocalDate last365Days = today.minusDays(365);

        long admissionsToday = Optional.ofNullable(hostelFormRepository.countByExactDate(today)).orElse(0L);
        long admissionsLast7Days = Optional.ofNullable(hostelFormRepository.countByDateBetween(last7Days, today)).orElse(0L);
        long admissionsLast30Days = Optional.ofNullable(hostelFormRepository.countByDateBetween(last30Days, today)).orElse(0L);
        long admissionsLast365Days = Optional.ofNullable(hostelFormRepository.countByDateBetween(last365Days, today)).orElse(0L);
        long totalAdmissions = Optional.ofNullable(hostelFormRepository.count()).orElse(0L);


        Double revenueToday = Optional.ofNullable(hostelFormRepository.getRevenueByExactDate(today)).orElse(0.0);
        Double revenueLast7Days = Optional.ofNullable(hostelFormRepository.getRevenueByDateRange(last7Days, today)).orElse(0.0);
        Double revenueLast30Days = Optional.ofNullable(hostelFormRepository.getRevenueByDateRange(last30Days, today)).orElse(0.0);
        Double revenueLast365Days = Optional.ofNullable(hostelFormRepository.getRevenueByDateRange(last365Days, today)).orElse(0.0);
        Double totalRevenue = Optional.ofNullable(hostelFormRepository.getTotalRevenue()).orElse(0.0);


        Map<String, Object> response = new HashMap<>();
        response.put("today", Map.of("count", admissionsToday, "revenue", revenueToday));
        response.put("last7Days", Map.of("count", admissionsLast7Days, "revenue", revenueLast7Days));
        response.put("last30Days", Map.of("count", admissionsLast30Days, "revenue", revenueLast30Days));
        response.put("last365Days", Map.of("count", admissionsLast365Days, "revenue", revenueLast365Days));
        response.put("total", Map.of("count", totalAdmissions, "revenue", totalRevenue));

        return response;
    }


    // ✅ Fetch Admissions and Revenue for a Specific Month & Year
    @Override
    public Map<String, Object> getMonthlyStats(int year, String requestPath) {
        List<Object[]> results = hostelFormRepository.getMonthlyAdmissionsAndRevenue(year);

        if (results.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No admissions found for " + year);
        }

        List<Map<String, Object>> monthlyAdmissions = new ArrayList<>();

        for (Object[] result : results) {
            int month = (Integer) result[0];
            long admissions = ((Number) result[1]).longValue();
            double revenue = ((Number) result[2]).doubleValue();

            monthlyAdmissions.add(Map.of(
                    "year", year,
                    "month", Month.of(month).name(),
                    "admissions", admissions,
                    "revenue", revenue
            ));
        }

        return Map.of("monthlyAdmissions", monthlyAdmissions);
    }







}
