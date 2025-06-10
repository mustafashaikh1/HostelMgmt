package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.UndertakingForm;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Repository.UndertakingFormRepository;
import com.Hostel.Service.HostelFormService;
import com.Hostel.Service.S3Service;
import com.Hostel.Service.UndertakingFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class UndertakingFormServiceImpl implements UndertakingFormService {

    @Autowired
    private UndertakingFormRepository undertakingFormRepository;

    @Autowired
    private HostelFormRepository hostelFormRepository;

    @Autowired
    private HostelFormService hostelFormService;

    @Autowired
    private S3Service s3Service; // AWS S3 for image handling

    @Override
    public UndertakingForm saveUndertakingForm(UndertakingForm undertakingForm,
                                               MultipartFile otherProofImage,
                                               MultipartFile parentOtherProofImage,
                                               MultipartFile educationDocumentsImage,
                                               MultipartFile applicantSignatureImage,
                                               MultipartFile parentSignatureImage,
                                               String formNumber,
                                               String otherProofType,
                                               String parentOtherProofType,
                                               String educationDocumentsType,
                                               String stayFrom,
                                               String stayTo) throws IOException {

        HostelForm hostelForm = hostelFormRepository.findByFormNumber(formNumber)
                .orElseThrow(() -> new RuntimeException("HostelForm not found with formNumber: " + formNumber));

        // Upload images to AWS S3
        if (otherProofImage != null) {
            undertakingForm.setOtherProofImages(s3Service.uploadImage(otherProofImage));
        }
        if (parentOtherProofImage != null) {
            undertakingForm.setParentOtherProofImages(s3Service.uploadImage(parentOtherProofImage));
        }
        undertakingForm.setEducationDocumentsImages(s3Service.uploadImage(educationDocumentsImage));
        undertakingForm.setApplicantSignatureImage(s3Service.uploadImage(applicantSignatureImage));
        if (parentSignatureImage != null) {
            undertakingForm.setParentSignatureImage(s3Service.uploadImage(parentSignatureImage));
        }

        undertakingForm.setOtherProofType(otherProofType);
        undertakingForm.setParentOtherProofType(parentOtherProofType);
        undertakingForm.setEducationDocumentsType(educationDocumentsType); // Set new field

        // Convert dates
        undertakingForm.setStayFrom(LocalDate.parse(stayFrom));
        undertakingForm.setStayTo(LocalDate.parse(stayTo));

        // Associate forms
        undertakingForm.setHostelForm(hostelForm);
        hostelForm.setUndertakingForm(undertakingForm);

        undertakingFormRepository.save(undertakingForm);
        hostelFormRepository.save(hostelForm);
        hostelFormService.saveHostelForm(hostelForm);

        return undertakingForm;
    }


    @Override
    public List<UndertakingForm> getAllUndertakingForms() {
        return undertakingFormRepository.findAll();
    }

    @Override
    public UndertakingForm getUndertakingFormById(Long id) {
        return undertakingFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UndertakingForm not found with ID: " + id));
    }

    @Override
    public UndertakingForm getUndertakingFormByFormNumber(String formNumber) {
        HostelForm hostelForm = hostelFormRepository.findByFormNumber(formNumber)
                .orElseThrow(() -> new RuntimeException("HostelForm not found with formNumber: " + formNumber));

        return hostelForm.getUndertakingForm();
    }

    @Override
    public UndertakingForm updateUndertakingForm(Long id,
                                                 UndertakingForm updatedForm,
                                                 MultipartFile otherProofImage,
                                                 MultipartFile parentOtherProofImage,
                                                 MultipartFile educationDocumentsImage,
                                                 MultipartFile applicantSignatureImage,
                                                 MultipartFile parentSignatureImage,
                                                 String formNumber,
                                                 String otherProofType,
                                                 String parentOtherProofType,
                                                 String educationDocumentsType,
                                                 String stayFrom,
                                                 String stayTo) throws IOException {

        UndertakingForm existingForm = undertakingFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UndertakingForm not found with ID: " + id));

        // Update images if provided
        if (otherProofImage != null) {
            s3Service.deleteImage(existingForm.getOtherProofImages());
            existingForm.setOtherProofImages(s3Service.uploadImage(otherProofImage));
        }
        if (parentOtherProofImage != null) {
            s3Service.deleteImage(existingForm.getParentOtherProofImages());
            existingForm.setParentOtherProofImages(s3Service.uploadImage(parentOtherProofImage));
        }
        if (educationDocumentsImage != null) {
            s3Service.deleteImage(existingForm.getEducationDocumentsImages());
            existingForm.setEducationDocumentsImages(s3Service.uploadImage(educationDocumentsImage));
        }
        if (applicantSignatureImage != null) {
            s3Service.deleteImage(existingForm.getApplicantSignatureImage());
            existingForm.setApplicantSignatureImage(s3Service.uploadImage(applicantSignatureImage));
        }
        if (parentSignatureImage != null) {
            s3Service.deleteImage(existingForm.getParentSignatureImage());
            existingForm.setParentSignatureImage(s3Service.uploadImage(parentSignatureImage));
        }

        // Update other fields
        existingForm.setOtherProofType(otherProofType);
        existingForm.setParentOtherProofType(parentOtherProofType);
        existingForm.setEducationDocumentsType(educationDocumentsType); // Update new field
        existingForm.setStayFrom(LocalDate.parse(stayFrom));
        existingForm.setStayTo(LocalDate.parse(stayTo));

        return undertakingFormRepository.save(existingForm);
    }


    @Override
    public void deleteUndertakingForm(Long id) {
        UndertakingForm undertakingForm = undertakingFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UndertakingForm not found with ID: " + id));

        // Delete images from S3 (only for existing images)
        if (undertakingForm.getOtherProofImages() != null) {
            s3Service.deleteImage(undertakingForm.getOtherProofImages());
        }
        if (undertakingForm.getParentOtherProofImages() != null) {
            s3Service.deleteImage(undertakingForm.getParentOtherProofImages());
        }
        if (undertakingForm.getEducationDocumentsImages() != null) {
            s3Service.deleteImage(undertakingForm.getEducationDocumentsImages());
        }
        if (undertakingForm.getApplicantSignatureImage() != null) {
            s3Service.deleteImage(undertakingForm.getApplicantSignatureImage());
        }
        if (undertakingForm.getParentSignatureImage() != null) {
            s3Service.deleteImage(undertakingForm.getParentSignatureImage());
        }

        // Remove association from HostelForm
        HostelForm hostelForm = undertakingForm.getHostelForm();
        if (hostelForm != null) {
            hostelForm.setUndertakingForm(null);
            hostelFormRepository.save(hostelForm);
        }

        // Delete UndertakingForm
        undertakingFormRepository.deleteById(id);
    }
}
