package com.Hostel.Service;

import com.Hostel.Entity.UndertakingForm;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UndertakingFormService {

    // Save UndertakingForm with images and additional fields
    UndertakingForm saveUndertakingForm(UndertakingForm undertakingForm,
                                        MultipartFile otherProofImage,
                                        MultipartFile parentOtherProofImage,
                                        MultipartFile educationDocumentsImage,
                                        MultipartFile applicantSignatureImage,
                                        MultipartFile parentSignatureImage,
                                        String formNumber,
                                        String otherProofType,
                                        String parentOtherProofType,
                                        String educationDocumentsType, // New parameter
                                        String stayFrom,
                                        String stayTo) throws IOException;

    // Get all UndertakingForms
    List<UndertakingForm> getAllUndertakingForms();

    // Get UndertakingForm by ID
    UndertakingForm getUndertakingFormById(Long id);

    // Get UndertakingForm by formNumber
    UndertakingForm getUndertakingFormByFormNumber(String formNumber);

    // Update UndertakingForm with images and additional fields
    UndertakingForm updateUndertakingForm(Long id,
                                          UndertakingForm updatedForm,
                                          MultipartFile otherProofImage,
                                          MultipartFile parentOtherProofImage,
                                          MultipartFile educationDocumentsImage,
                                          MultipartFile applicantSignatureImage,
                                          MultipartFile parentSignatureImage,
                                          String formNumber,
                                          String otherProofType,
                                          String parentOtherProofType,
                                          String educationDocumentsType, // New parameter
                                          String stayFrom,
                                          String stayTo) throws IOException;

    // Delete UndertakingForm
    void deleteUndertakingForm(Long id);
}
