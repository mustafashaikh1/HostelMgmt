package com.Hostel.Service;

import com.Hostel.Entity.HostelForm;

import java.util.List;
import java.util.Map;

public interface HostelFormService {
    HostelForm saveHostelForm(HostelForm hostelForm);
    List<HostelForm> getAllHostelForms();
    HostelForm getHostelFormById(Long HostelFormId);
    HostelForm getHostelFormByFormNumber(String formNumber);
    HostelForm updateHostelForm(Long HostelFormId, HostelForm hostelForm);
    void deleteHostelForm(Long HostelFormId);
    void deleteHostelFormByFormNumber(String formNumber);

    Map<String, Object> getAdmissionStats();
    Map<String, Object> getMonthlyStats(int year, String requestPath);

}
