package com.Hostel.Service;

import com.Hostel.Entity.AdmissionForm;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdmissionFormService {
    AdmissionForm saveAdmissionForm(AdmissionForm admissionForm, MultipartFile signatureFile,
                                    String formNumber, String studentName, String email, String mobileNo,
                                    String roomNumber, String roomType, String bedType, String bedNumber,
                                    String floor, String monthName, Double monthRent, String paymentMode,
                                    String year, String admissionDate, String conductedBy, String source,
                                    String paymentStatus, String transactionNumber, Boolean gstIncluded,
                                    Double gstPercentage, String gstNumber, String fromDate, String toDate,
                                    Integer numberOfMonths) throws IOException;


    List<AdmissionForm> getAllAdmissionForms();

    Optional<AdmissionForm> getAdmissionFormById(Long id);

    List<AdmissionForm> getAdmissionFormByFormNumber(String formNumber);
    AdmissionForm updateAdmissionForm(Long id, AdmissionForm admissionForm, MultipartFile signatureFile) throws IOException;

    void deleteAdmissionForm(Long id);

    Map<String, Object> getDateWiseAdmissionStats();

    Map<String, Long> getSourceWiseAdmissions(LocalDate startDate, LocalDate endDate);

    Map<String, Object> getRevenueStats();
    List<Map<String, Object>> getMonthWiseAdmissionStatsByYear(Integer year);
    Map<String, Object> getYearWiseAdmissionStatsByYear(int year);

    List<Map<String, Object>> getDateWiseSummaryWithMonth(String monthName, String year);

}