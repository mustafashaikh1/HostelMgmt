package com.Hostel.Service;

import com.Hostel.Entity.MedicalInformation;

import java.util.List;

public interface MedicalInformationService {

    MedicalInformation saveMedicalInformation(MedicalInformation medicalInformation,String formNumber);

    MedicalInformation updateMedicalInformation(Long medicalInfoId, MedicalInformation medicalInformation);

    MedicalInformation getMedicalInformationById(Long medicalInfoId);

    List<MedicalInformation> getAllMedicalInformation();

    void deleteMedicalInformation(Long medicalInfoId);
}
