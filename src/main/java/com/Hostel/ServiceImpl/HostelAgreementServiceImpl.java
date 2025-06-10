package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelAgreement;
import com.Hostel.Entity.HostelForm;
import com.Hostel.Repository.HostelAgreementRepository;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Service.HostelAgreementService;
import com.Hostel.Service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class HostelAgreementServiceImpl implements HostelAgreementService {

    private final HostelAgreementRepository hostelAgreementRepository;
    private final HostelFormRepository hostelFormRepository;
    private final S3Service s3Service;

    public HostelAgreementServiceImpl(HostelAgreementRepository hostelAgreementRepository,
                                      HostelFormRepository hostelFormRepository, S3Service s3Service) {
        this.hostelAgreementRepository = hostelAgreementRepository;
        this.hostelFormRepository = hostelFormRepository;
        this.s3Service = s3Service;
    }

    @Override
    @Transactional
    public HostelAgreement saveHostelAgreement(String formNumber, String applicantName, MultipartFile applicantSignature,
                                               String parentName, MultipartFile parentSignature) throws IOException {
        HostelForm hostelForm = hostelFormRepository.findByFormNumber(formNumber)
                .orElseThrow(() -> new RuntimeException("Hostel form not found for form number: " + formNumber));

        // Check if agreement already exists for the form
        Optional<HostelAgreement> existingAgreement = hostelAgreementRepository.findByHostelForm(hostelForm);
        if (existingAgreement.isPresent()) {
            throw new RuntimeException("Agreement already exists for form number: " + formNumber);
        }

        String applicantSignatureUrl = s3Service.uploadImage(applicantSignature);
        String parentSignatureUrl = s3Service.uploadImage(parentSignature);

        HostelAgreement agreement = new HostelAgreement();
        agreement.setHostelForm(hostelForm);
        agreement.setApplicantName(applicantName);
        agreement.setApplicantSignatureUrl(applicantSignatureUrl);
        agreement.setParentName(parentName);
        agreement.setParentSignatureUrl(parentSignatureUrl);

        return hostelAgreementRepository.save(agreement);
    }

    @Override
    public HostelAgreement getHostelAgreementById(Long id) {
        return hostelAgreementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hostel Agreement not found for ID: " + id));
    }

    @Override
    public HostelAgreement getHostelAgreementByFormNumber(String formNumber) {
        HostelAgreement agreement = hostelAgreementRepository.findByHostelForm_FormNumber(formNumber);

        // If no agreement is found, throw an exception
        if (agreement == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hostel Agreement not found for form number: " + formNumber);
        }

        // Return the found agreement
        return agreement;
    }

    @Override
    public List<HostelAgreement> getAllHostelAgreements() {
        return hostelAgreementRepository.findAll();
    }

    @Override
    @Transactional
    public HostelAgreement updateHostelAgreement(Long id, String applicantName, MultipartFile applicantSignature,
                                                 String parentName, MultipartFile parentSignature) throws IOException {
        // Fetch the existing agreement from the database
        HostelAgreement agreement = hostelAgreementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hostel Agreement not found"));

        // If a new applicant signature is uploaded, upload it to S3 and update the URL in the entity
        if (applicantSignature != null) {
            // Upload the new applicant signature image
            String newApplicantSignatureUrl = s3Service.uploadImage(applicantSignature);
            agreement.setApplicantSignatureUrl(newApplicantSignatureUrl); // Update the URL in the entity
        }

        // If a new parent signature is uploaded, upload it to S3 and update the URL in the entity
        if (parentSignature != null) {
            // Upload the new parent signature image
            String newParentSignatureUrl = s3Service.uploadImage(parentSignature);
            agreement.setParentSignatureUrl(newParentSignatureUrl); // Update the URL in the entity
        }

        // Update the applicant and parent names if changed
        agreement.setApplicantName(applicantName);
        agreement.setParentName(parentName);

        // Save the updated agreement in the database
        return hostelAgreementRepository.save(agreement);
    }

    @Override
    @Transactional
    public void deleteHostelAgreement(String formNumber) {
        Optional<HostelAgreement> agreementOpt = Optional.ofNullable(hostelAgreementRepository.findByHostelForm_FormNumber(formNumber));

        if (agreementOpt.isPresent()) {
            HostelAgreement agreement = agreementOpt.get();

            // If necessary, you can add logic here to delete images from S3 if you want to.
            // s3Service.deleteImage(agreement.getApplicantSignatureUrl());
            // s3Service.deleteImage(agreement.getParentSignatureUrl());

            hostelAgreementRepository.delete(agreement); // Delete from DB but leave images in S3
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hostel Agreement not found for form number: " + formNumber);
        }
    }

}

