package com.Hostel.ServiceImpl;

import com.Hostel.Entity.DepositeDetails;
import com.Hostel.Entity.HostelForm;
import com.Hostel.Repository.DepositeDetailsRepository;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Service.DepositeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class DepositeDetailsServiceImpl implements DepositeDetailsService {

    @Autowired
    private DepositeDetailsRepository depositeDetailsRepository;

    @Autowired
    private HostelFormRepository hostelFormRepository;

    @Override
    public DepositeDetails saveDepositeDetails(String formNumber, DepositeDetails depositeDetails) {
        HostelForm hostelForm = hostelFormRepository.findByFormNumber(formNumber)
                .orElseThrow(() -> new RuntimeException("Hostel form not found for form number: " + formNumber));

        // Initialize new fields if null
        if (depositeDetails.getPendingAmount() == null) depositeDetails.setPendingAmount(0.0);
        if (depositeDetails.getCancelAmount() == null) depositeDetails.setCancelAmount(0.0);
        if (depositeDetails.getRefundAmount() == null) depositeDetails.setRefundAmount(0.0);

        depositeDetails.setHostelForm(hostelForm);
        return depositeDetailsRepository.save(depositeDetails);
    }


    @Override
    public DepositeDetails updateDepositDetails(Long id, DepositeDetails updatedDetails) {
        DepositeDetails existing = depositeDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DepositeDetails not found for id: " + id));

        existing.setDepositeAmount(updatedDetails.getDepositeAmount());
        existing.setPendingAmount(updatedDetails.getPendingAmount());
        existing.setCancelAmount(updatedDetails.getCancelAmount());
        existing.setRefundAmount(updatedDetails.getRefundAmount());
        existing.setDepositeStatus(updatedDetails.getDepositeStatus());
        existing.setPaymentMode(updatedDetails.getPaymentMode());
        existing.setTransactionNumber(updatedDetails.getTransactionNumber());
        existing.setConductedBy(updatedDetails.getConductedBy());
        existing.setGstIncluded(updatedDetails.getGstIncluded());
        existing.setGstNumber(updatedDetails.getGstNumber());
        existing.setGstPercentage(updatedDetails.getGstPercentage());
        existing.setMonthName(updatedDetails.getMonthName());
        existing.setYear(updatedDetails.getYear());
        existing.setPendingAmount(updatedDetails.getPendingAmount());
        existing.setCancelAmount(updatedDetails.getCancelAmount());
        existing.setRefundAmount(updatedDetails.getRefundAmount());


        return depositeDetailsRepository.save(existing);
    }

    @Override
    public List<DepositeDetails> getAllDepositeDetails() {
        return depositeDetailsRepository.findAll();
    }

    @Override
    public DepositeDetails getDepositeDetailsById(Long id) {
        return depositeDetailsRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public void deleteDepositeDetails(Long id) {
        depositeDetailsRepository.deleteById(id);
    }


    @Override
    public List<Map<String, Object>> getRevenueSummary() {
        LocalDate today = LocalDate.now();
        LocalDate last7Days = today.minusDays(6);
        LocalDate last30Days = today.minusDays(29);
        LocalDate last365Days = today.minusDays(364);

        List<Map<String, Object>> summaryList = new ArrayList<>();

        summaryList.add(buildSummary("Today", depositeDetailsRepository.findByCreatedDate(today)));
        summaryList.add(buildSummary("Last 7 Days", depositeDetailsRepository.findByCreatedDateBetween(last7Days, today)));
        summaryList.add(buildSummary("Last 30 Days", depositeDetailsRepository.findByCreatedDateBetween(last30Days, today)));
        summaryList.add(buildSummary("Last 365 Days", depositeDetailsRepository.findByCreatedDateBetween(last365Days, today)));
        summaryList.add(buildSummary("Total", depositeDetailsRepository.findAll()));

        return summaryList;
    }

    private Map<String, Object> buildSummary(String label, List<DepositeDetails> list) {
        double total = list.stream().mapToDouble(DepositeDetails::getTotalAmount).sum();
        Map<String, Object> map = new HashMap<>();
        map.put("label", label);
        map.put("totalAmount", total);
        map.put("count", list.size());
        return map;
    }

    @Override
    public List<Object[]> getDepositSummaryByDate(String monthName, String year) {
        return depositeDetailsRepository.getDepositSummaryByDate(monthName, year);
    }


    @Override
    public List<Map<String, Object>> getMonthWiseSummary(String year) {
        List<Object[]> rawData = depositeDetailsRepository.getMonthWiseDepositSummary(year);

        List<Map<String, Object>> formattedList = new ArrayList<>();

        for (Object[] row : rawData) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("month", row[0]);
            map.put("count", row[1]);
            map.put("totalAmount", row[2]);
            formattedList.add(map);
        }

        return formattedList;
    }


}
