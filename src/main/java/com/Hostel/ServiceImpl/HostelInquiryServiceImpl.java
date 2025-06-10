package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelInquiry;
import com.Hostel.Entity.HostelInquiryStatus;
import com.Hostel.Repository.HostelInquiryRepository;
import com.Hostel.Repository.HostelInquiryStatusRepository;
import com.Hostel.Service.HostelInquiryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HostelInquiryServiceImpl implements HostelInquiryService {

    @Autowired
    private HostelInquiryRepository hostelInquiryRepository;

    @Autowired
    private HostelInquiryStatusRepository hostelInquiryStatusRepository;

    @Override
    public HostelInquiry saveHostelInquiry(HostelInquiry hostelInquiry) {
        return hostelInquiryRepository.save(hostelInquiry);
    }

    @Override
    public List<HostelInquiry> getAllHostelInquiries() {
        return hostelInquiryRepository.findAll();
    }

    @Override
    public HostelInquiry getHostelInquiryById(Long inquiryId) {
        return hostelInquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inquiry not found with ID: " + inquiryId));
    }

    @Override
    public List<HostelInquiry> getHostelInquiriesByStudentName(String studentName) {
        return hostelInquiryRepository.findByStudentName(studentName);
    }

    @Override
    public List<HostelInquiry> getHostelInquiriesByCity(String city) {
        return hostelInquiryRepository.findByCity(city);
    }



    @Override
    public List<HostelInquiry> getHostelInquiriesByEmail(String email) {
        return hostelInquiryRepository.findByEmail(email);
    }

    @Override
    public HostelInquiry updateHostelInquiry(Long inquiryId, HostelInquiry hostelInquiry) {
        if (!hostelInquiryRepository.existsById(inquiryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inquiry not found with ID: " + inquiryId);
        }
        hostelInquiry.setInquiryId(inquiryId);
        return hostelInquiryRepository.save(hostelInquiry);
    }

    @Override
    public void deleteHostelInquiry(Long inquiryId) {
        if (!hostelInquiryRepository.existsById(inquiryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inquiry not found with ID: " + inquiryId);
        }
        hostelInquiryRepository.deleteById(inquiryId);
    }

    @Transactional
    @Override
    public HostelInquiry updateInquiryStatus(Long inquiryId, String status) {
        HostelInquiry inquiry = getHostelInquiryById(inquiryId);
        inquiry.setCurrentStatus(status);

        HostelInquiryStatus inquiryStatus = new HostelInquiryStatus();
        inquiryStatus.setHostelInquiry(inquiry);
        inquiryStatus.setStatus(status);

        hostelInquiryStatusRepository.save(inquiryStatus);
        return hostelInquiryRepository.save(inquiry);
    }

    @Override
    public List<HostelInquiry> getHostelInquiriesByCurrentStatus(String currentStatus) {
        List<HostelInquiry> inquiries = hostelInquiryRepository.findByCurrentStatus(currentStatus);
        if (inquiries.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No inquiries found with status: " + currentStatus);
        }
        return inquiries;
    }




    @Override
    public Map<String, Object> getInquiryStats() {
        LocalDate today = LocalDate.now();
        LocalDate last7Days = today.minusDays(7);
        LocalDate last30Days = today.minusDays(30);
        LocalDate last365Days = today.minusDays(365);

        long inquiriesToday = hostelInquiryRepository.countByExactDate(today);
        long inquiriesLast7Days = hostelInquiryRepository.countByDateBetween(last7Days, today);
        long inquiriesLast30Days = hostelInquiryRepository.countByDateBetween(last30Days, today);
        long inquiriesLast365Days = hostelInquiryRepository.countByDateBetween(last365Days, today);
        long totalInquiries = hostelInquiryRepository.count();

        // Ensure null values are replaced with 0
        inquiriesToday = (inquiriesToday > 0) ? inquiriesToday : 0;
        inquiriesLast7Days = (inquiriesLast7Days > 0) ? inquiriesLast7Days : 0;
        inquiriesLast30Days = (inquiriesLast30Days > 0) ? inquiriesLast30Days : 0;
        inquiriesLast365Days = (inquiriesLast365Days > 0) ? inquiriesLast365Days : 0;
        totalInquiries = (totalInquiries > 0) ? totalInquiries : 0;

        Map<String, Object> response = new HashMap<>();
        response.put("today", Map.of("Inquiry", inquiriesToday));
        response.put("last7Days", Map.of("Inquiry", inquiriesLast7Days));
        response.put("last30Days", Map.of("Inquiry", inquiriesLast30Days));
        response.put("last365Days", Map.of("Inquiry", inquiriesLast365Days));
        response.put("total", Map.of("Inquiry", totalInquiries));

        return response;
    }



    @Override
    public Map<String, Object> getMonthlyInquiryStats(int year, String requestPath) {
        List<Object[]> results = hostelInquiryRepository.getMonthlyInquiries(year);

        if (results.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No inquiries found for " + year);
        }

        List<Map<String, Object>> monthlyInquiries = new ArrayList<>();

        for (Object[] result : results) {
            int month = (Integer) result[0];
            long inquiries = ((Number) result[1]).longValue();

            monthlyInquiries.add(Map.of(
                    "year", year,
                    "month", Month.of(month).name(),
                    "inquiries", inquiries
            ));
        }

        return Map.of("monthlyInquiries", monthlyInquiries);
    }

    @Override
    public Map<String, Long> getSourceWiseInquiries(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = hostelInquiryRepository.countInquiriesBySourceBetweenDates(startDate, endDate);
        Map<String, Long> sourceWiseMap = new HashMap<>();

        for (Object[] row : results) {
            String source = (String) row[0];
            Long count = (Long) row[1];
            sourceWiseMap.put(source, count);
        }

        return sourceWiseMap;
    }

    @Override
    public List<Object[]> getInquirySummaryByMonthAndYear(String monthName, String year) {
        return hostelInquiryRepository.getInquiryCountByMonthAndYear(monthName, year);
    }


}
