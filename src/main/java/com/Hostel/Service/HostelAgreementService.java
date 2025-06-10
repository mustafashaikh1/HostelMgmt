package com.Hostel.Service;

import com.Hostel.Entity.HostelAgreement;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HostelAgreementService {

    // Save a new Hostel Agreement
    HostelAgreement saveHostelAgreement(String formNumber, String applicantName, MultipartFile applicantSignature,
                                        String parentName, MultipartFile parentSignature) throws IOException;


    HostelAgreement getHostelAgreementById(Long id);

    // Get a single Hostel Agreement by form number
    HostelAgreement getHostelAgreementByFormNumber(String formNumber);

    // Get all Hostel Agreements
    List<HostelAgreement> getAllHostelAgreements();

    // Update an existing Hostel Agreement
    HostelAgreement updateHostelAgreement(Long id, String applicantName, MultipartFile applicantSignature,
                                          String parentName, MultipartFile parentSignature) throws IOException;

    // Delete a Hostel Agreement by form number
    void deleteHostelAgreement(String formNumber);
}
