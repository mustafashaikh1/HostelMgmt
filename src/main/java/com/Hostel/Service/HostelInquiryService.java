package com.Hostel.Service;



import com.Hostel.Entity.HostelInquiry;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HostelInquiryService {

    HostelInquiry saveHostelInquiry(HostelInquiry hostelInquiry);
    List<HostelInquiry> getAllHostelInquiries();
    HostelInquiry getHostelInquiryById(Long inquiryId);
    List<HostelInquiry> getHostelInquiriesByStudentName(String studentName);
    List<HostelInquiry> getHostelInquiriesByCity(String city);
    List<HostelInquiry> getHostelInquiriesByCurrentStatus(String currentStatus);
    List<HostelInquiry> getHostelInquiriesByEmail(String email);
    HostelInquiry updateHostelInquiry(Long inquiryId, HostelInquiry hostelInquiry);
    void deleteHostelInquiry(Long inquiryId);
    HostelInquiry updateInquiryStatus(Long inquiryId, String status);
    Map<String, Object> getInquiryStats();
    Map<String, Object> getMonthlyInquiryStats(int year, String requestPath);

    Map<String, Long> getSourceWiseInquiries(LocalDate startDate, LocalDate endDate);
    List<Object[]> getInquirySummaryByMonthAndYear(String monthName, String year);


}
