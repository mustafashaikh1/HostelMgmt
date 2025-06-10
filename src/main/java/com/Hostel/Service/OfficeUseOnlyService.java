package com.Hostel.Service;



import com.Hostel.Entity.OfficeUseOnly;
import com.Hostel.Entity.Payment;

import java.util.List;
import java.util.Map;

public interface OfficeUseOnlyService {
    OfficeUseOnly saveOfficeUseOnly(OfficeUseOnly officeUseOnly, String formNumber);
    List<OfficeUseOnly> getAllOfficeUseOnly();
    OfficeUseOnly getOfficeUseOnlyById(Long id);
    OfficeUseOnly updateOfficeUseOnly(Long id, OfficeUseOnly officeUseOnly);
    void deleteOfficeUseOnly(Long id);
    Map<String, Object> getDashboardStats();


    // Payment Management
    Map<String, Object> addPayment(String formNumber, Payment payment);
    List<Payment> getPaymentsByFormNumber(String formNumber);

    OfficeUseOnly getOfficeUseOnlyByFormNumber(String formNumber);
}