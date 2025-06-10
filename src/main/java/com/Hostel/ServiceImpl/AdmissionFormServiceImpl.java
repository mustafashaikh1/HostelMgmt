package com.Hostel.ServiceImpl;

import com.Hostel.Entity.AdmissionForm;
import com.Hostel.Entity.Bed;
import com.Hostel.Repository.AdmissionFormRepository;
import com.Hostel.Repository.BedRepository;
import com.Hostel.Service.AdmissionFormService;
import com.Hostel.Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdmissionFormServiceImpl implements AdmissionFormService {

    @Autowired
    private AdmissionFormRepository admissionFormRepository;

    @Autowired
    private BedRepository bedRepository;


    @Autowired
    private S3Service s3Service;


    @Override
    public AdmissionForm saveAdmissionForm(AdmissionForm admissionForm, MultipartFile signatureFile,
                                           String formNumber, String studentName, String email, String mobileNo,
                                           String roomNumber, String roomType, String bedType, String bedNumber,
                                           String floor, String monthName, Double monthRent, String paymentMode,
                                           String year, String admissionDate, String conductedBy,
                                           String source, String paymentStatus, String transactionNumber,
                                           Boolean gstIncluded, Double gstPercentage, String gstNumber,
                                           String fromDate, String toDate, Integer numberOfMonths) throws IOException {
        // ... existing setup code ...

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
        admissionForm.setTransactionNumber(transactionNumber);
        admissionForm.setGstIncluded(gstIncluded);
        admissionForm.setGstPercentage(gstPercentage);
        admissionForm.setGstNumber(gstNumber);
        admissionForm.setFromDate(LocalDate.parse(fromDate));
        admissionForm.setToDate(LocalDate.parse(toDate));
        admissionForm.setNumberOfMonths(Double.valueOf(numberOfMonths));
        admissionForm.setTotalRent(monthRent * numberOfMonths);

        if (Boolean.TRUE.equals(gstIncluded) && gstPercentage != null) {
            double gstAmount = (monthRent * numberOfMonths * gstPercentage) / 100;
            double totalAmount = monthRent * numberOfMonths + gstAmount;
            admissionForm.setGstAmount(gstAmount);
            admissionForm.setTotalAmount(totalAmount);
        } else {
            admissionForm.setGstAmount(0.0);
            admissionForm.setTotalAmount(monthRent * numberOfMonths);
        }

        // FIXED: Set isActive to true when saving admission form
        // This ensures that newly created admissions are always active
        admissionForm.setIsActive(true);

        // Handle bed allocation - allocate bed when saving the form
        allocateBed(bedNumber);
        admissionForm.setBedAllocationStatus("Allocated");

        return admissionFormRepository.save(admissionForm);
    }

    // Optional: Method to update isActive status based on dates (can be called by scheduler)
    public void updateAdmissionActiveStatus() {
        List<AdmissionForm> allAdmissions = admissionFormRepository.findAll();
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Kolkata"));

        for (AdmissionForm admission : allAdmissions) {
            boolean shouldBeActive = !currentDate.isBefore(admission.getFromDate()) &&
                    !currentDate.isAfter(admission.getToDate());

            if (admission.getIsActive() != shouldBeActive) {
                admission.setIsActive(shouldBeActive);

                // Update bed allocation status based on active status
                if (shouldBeActive && !"Allocated".equals(admission.getBedAllocationStatus())) {
                    allocateBed(admission.getBedNumber());
                    admission.setBedAllocationStatus("Allocated");
                } else if (!shouldBeActive && "Allocated".equals(admission.getBedAllocationStatus())) {
                    deallocateBed(admission.getBedNumber());
                    admission.setBedAllocationStatus("Not Allocated");
                }

                admissionFormRepository.save(admission);
            }
        }
    }


    private void allocateBed(String bedNumber) {
        Bed bed = bedRepository.findByBedNumber(bedNumber)
                .orElseThrow(() -> new RuntimeException("Bed not found with bedNumber: " + bedNumber));
        bed.setAllocated(true);
        bedRepository.save(bed);
    }

    private void deallocateBed(String bedNumber) {
        if (bedNumber != null) {
            bedRepository.findByBedNumber(bedNumber).ifPresent(bed -> {
                bed.setAllocated(false);
                bedRepository.save(bed);
            });
        }
    }

    // Scheduled task to check and update bed allocations daily at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkAndUpdateBedAllocations() {
        LocalDate today = LocalDate.now();
        List<AdmissionForm> allForms = admissionFormRepository.findAll();

        for (AdmissionForm form : allForms) {
            boolean shouldBeActive = !today.isBefore(form.getFromDate()) &&
                    !today.isAfter(form.getToDate());

            // Fix: Use direct boolean comparison instead of equals()
            if (shouldBeActive != form.getIsActive()) {
                form.setIsActive(shouldBeActive);
                form.setBedAllocationStatus(shouldBeActive ? "Allocated" : "Not Allocated");
                admissionFormRepository.save(form);

                // Update bed allocation
                if (shouldBeActive) {
                    allocateBed(form.getBedNumber());
                } else {
                    deallocateBed(form.getBedNumber());
                }
            }
        }
    }


    @Override
    public List<AdmissionForm> getAllAdmissionForms() {
        return admissionFormRepository.findAll();
    }

    @Override
    public Optional<AdmissionForm> getAdmissionFormById(Long id) {
        return admissionFormRepository.findById(id);
    }


    @Override
    public List<AdmissionForm> getAdmissionFormByFormNumber(String formNumber) {
        return admissionFormRepository.findAllByFormNumber(formNumber);
    }

    @Override
    public AdmissionForm updateAdmissionForm(Long id, AdmissionForm admissionForm, MultipartFile signatureFile) throws IOException {
        Optional<AdmissionForm> existingForm = admissionFormRepository.findById(id);

        if (existingForm.isPresent()) {
            AdmissionForm formToUpdate = existingForm.get();

            // Upload new signature file if provided
            if (signatureFile != null && !signatureFile.isEmpty()) {
                // Delete old signature from S3
                if (formToUpdate.getAuthorizedSignatureUrl() != null) {
                    s3Service.deleteImage(formToUpdate.getAuthorizedSignatureUrl());
                }
                String uploadedUrl = s3Service.uploadImage(signatureFile);
                formToUpdate.setAuthorizedSignatureUrl(uploadedUrl);
            }

            // Update the rest of the fields
            formToUpdate.setStudentName(admissionForm.getStudentName());
            formToUpdate.setEmail(admissionForm.getEmail());
            formToUpdate.setMobileNo(admissionForm.getMobileNo());
            formToUpdate.setRoomNumber(admissionForm.getRoomNumber());
            formToUpdate.setRoomType(admissionForm.getRoomType());
            formToUpdate.setBedType(admissionForm.getBedType());
            formToUpdate.setBedNumber(admissionForm.getBedNumber());
            formToUpdate.setFloor(admissionForm.getFloor());
            formToUpdate.setMonthName(admissionForm.getMonthName());
            formToUpdate.setMonthRent(admissionForm.getMonthRent());
            formToUpdate.setPaymentMode(admissionForm.getPaymentMode());
            formToUpdate.setYear(admissionForm.getYear());
            formToUpdate.setAdmissionDate(admissionForm.getAdmissionDate());
            formToUpdate.setConductedBy(admissionForm.getConductedBy());
            formToUpdate.setSource(admissionForm.getSource());
            formToUpdate.setPaymentStatus(admissionForm.getPaymentStatus());


            // Save the updated form
            return admissionFormRepository.save(formToUpdate);
        } else {
            // Return null if the form with the given ID is not found
            return null;
        }
    }


    @Override
    public void deleteAdmissionForm(Long id) {
        Optional<AdmissionForm> admissionForm = admissionFormRepository.findById(id);
        admissionForm.ifPresent(form -> {
            // Deallocate the bed if it's allocated
            if (form.getIsActive() != null && form.getIsActive() && form.getBedNumber() != null) {
                deallocateBed(form.getBedNumber());
            }
            // Delete the signature from S3 if it exists
            if (form.getAuthorizedSignatureUrl() != null) {
                s3Service.deleteImage(form.getAuthorizedSignatureUrl());
            }
            // Delete the admission form
            admissionFormRepository.deleteById(id);
        });
    }


    @Override
    public Map<String, Object> getDateWiseAdmissionStats() {
        List<Object[]> rawStats = admissionFormRepository.getDateWiseAdmissionStats();
        List<Map<String, Object>> formattedStats = new ArrayList<>();
        double totalRevenue = 0.0;

        for (Object[] row : rawStats) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0].toString()); // LocalDate to String
            map.put("admissionCount", row[1]);
            map.put("totalRevenue", row[2]);
            totalRevenue += (Double) row[2];
            formattedStats.add(map);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("AllTotalRevenue", totalRevenue);
        result.put("data", formattedStats);

        return result;
    }


    @Override
    public Map<String, Long> getSourceWiseAdmissions(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = admissionFormRepository.countAdmissionsBySourceBetweenDates(startDate, endDate);
        Map<String, Long> sourceWiseMap = new HashMap<>();

        for (Object[] row : results) {
            String source = (String) row[0];
            Long count = (Long) row[1];
            sourceWiseMap.put(source, count);
        }

        return sourceWiseMap;
    }


    public Map<String, Object> getRevenueStats() {
        LocalDate today = LocalDate.now();
        LocalDate last7Days = today.minusDays(7);
        LocalDate last30Days = today.minusDays(30);
        LocalDate last365Days = today.minusDays(365);

        List<AdmissionForm> allForms = admissionFormRepository.findAll();

        Map<String, Object> stats = new HashMap<>();

        stats.put("today", calculateStats(allForms, today, today));
        stats.put("last7Days", calculateStats(allForms, last7Days, today));
        stats.put("last30Days", calculateStats(allForms, last30Days, today));
        stats.put("last365Days", calculateStats(allForms, last365Days, today));
        stats.put("total", calculateStats(allForms, null, null)); // all data

        return stats;
    }

    private Map<String, Object> calculateStats(List<AdmissionForm> forms, LocalDate from, LocalDate to) {
        return forms.stream()
                .filter(f -> {
                    if (from == null || to == null) return true;
                    LocalDate date = f.getAdmissionDate();
                    return (date != null && !date.isBefore(from) && !date.isAfter(to));
                })
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            double totalRevenue = list.stream()
                                    .mapToDouble(f -> f.getTotalAmount() != null ? f.getTotalAmount() : 0.0)
                                    .sum();
                            Map<String, Object> map = new HashMap<>();
                            map.put("count", list.size());
                            map.put("revenue", totalRevenue);
                            return map;
                        }
                ));
    }

    @Override
    public List<Map<String, Object>> getMonthWiseAdmissionStatsByYear(Integer year) {
        List<Object[]> rawStats = admissionFormRepository.getMonthWiseAdmissionStatsByYear(year);
        List<Map<String, Object>> stats = new ArrayList<>();

        for (Object[] row : rawStats) {
            Integer resultYear = ((Number) row[0]).intValue();
            Integer month = ((Number) row[1]).intValue();
            Long count = ((Number) row[2]).longValue();
            Double totalRevenue = row[3] != null ? ((Number) row[3]).doubleValue() : 0.0;

            Map<String, Object> map = new HashMap<>();
            map.put("year", resultYear);
            map.put("month", Month.of(month).name()); // returns "JANUARY", etc.
            map.put("count", count);
            map.put("totalRevenue", totalRevenue);

            stats.add(map);
        }

        return stats;
    }


    private String getMonthName(int monthNumber) {
        return Month.of(monthNumber)
                .getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH); // Example: "April"
    }


    @Override
    public Map<String, Object> getYearWiseAdmissionStatsByYear(int year) {
        List<Object[]> rawStats = admissionFormRepository.getYearWiseAdmissionStatsByYear(year);
        List<Map<String, Object>> formattedStats = new ArrayList<>();
        double totalRevenue = 0.0;

        for (Object[] row : rawStats) {
            int yr = (int) row[0];
            long count = (long) row[1];
            double revenue = row[2] != null ? (double) row[2] : 0.0;

            Map<String, Object> map = new HashMap<>();
            map.put("year", yr);
            map.put("admissionCount", count);
            map.put("totalRevenue", revenue);
            formattedStats.add(map);

            totalRevenue += revenue;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("SelectedYearTotalRevenue", totalRevenue);
        result.put("data", formattedStats);

        return result;
    }


    @Override
    public List<Map<String, Object>> getDateWiseSummaryWithMonth(String monthName, String year) {
        List<Object[]> results = admissionFormRepository.getDateWiseSummaryWithMonth(monthName, year);
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0]);           // LocalDate
            map.put("month", row[1]);          // Month name
            map.put("count", row[2]);          // Long
            map.put("totalRevenue", row[3]);   // Double
            responseList.add(map);
        }

        return responseList;
    }

}
