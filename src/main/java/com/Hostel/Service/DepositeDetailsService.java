package com.Hostel.Service;

import com.Hostel.Entity.DepositeDetails;

import java.util.List;
import java.util.Map;

public interface DepositeDetailsService {
    DepositeDetails saveDepositeDetails(String formNumber, DepositeDetails depositeDetails);

    DepositeDetails updateDepositDetails(Long id, DepositeDetails updatedDetails);

    List<DepositeDetails> getAllDepositeDetails();

    DepositeDetails getDepositeDetailsById(Long id);

    void deleteDepositeDetails(Long id);

    List<Map<String, Object>> getRevenueSummary();

    List<Object[]> getDepositSummaryByDate(String monthName, String year);

    List<Map<String, Object>> getMonthWiseSummary(String year);
}
