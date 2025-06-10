package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.MedicalInformation;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Repository.MedicalInformationRepository;
import com.Hostel.Service.HostelFormService;
import com.Hostel.Service.MedicalInformationService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalInformationServiceImpl implements MedicalInformationService {

    @Autowired
    private MedicalInformationRepository medicalInformationRepository;

    @Autowired
    private HostelFormService hostelFormService;

    @Autowired
    private HostelFormRepository hostelFormRepository;


    @Override
    public MedicalInformation saveMedicalInformation(MedicalInformation medicalInformation,String formNumber) {
        // Fetch the HostelForm by formNumber
        HostelForm hostelForm = hostelFormService.getHostelFormByFormNumber(formNumber);

        // If HostelForm does not exist, throw an exception
        if (hostelForm == null) {
            throw new RuntimeException("HostelForm not found with formNumber: " + formNumber);
        }

        // Initialize lazy-loaded fields manually to avoid lazy loading issues
        Hibernate.initialize(hostelForm.getPersonalInfo());
        Hibernate.initialize(hostelForm.getFamilyDetails());


        // Set the HostelForm to the LocalGuardianDetails object
        medicalInformation.setHostelForm(hostelForm);

        // Save the LocalGuardianDetails directly using the repository
        MedicalInformation saveMedicalInformation = medicalInformationRepository.save(medicalInformation);

        // Optionally, update the HostelForm with the saved LocalGuardianDetails if required
        hostelForm.setMedicalInformation(saveMedicalInformation);
        hostelFormService.saveHostelForm(hostelForm); // Save the updated HostelForm

        // Return the saved LocalGuardianDetails object
        return saveMedicalInformation;

    }



    @Override
    public MedicalInformation updateMedicalInformation(Long medicalInfoId, MedicalInformation medicalInformation) {
        Optional<MedicalInformation> existingMedicalInfo = medicalInformationRepository.findById(medicalInfoId);

        if (existingMedicalInfo.isPresent()) {
            medicalInformation.setMedicalInfoId(medicalInfoId);
            return medicalInformationRepository.save(medicalInformation);
        } else {
            throw new RuntimeException("Medical Information not found for ID: " + medicalInfoId);
        }
    }

    @Override
    public MedicalInformation getMedicalInformationById(Long medicalInfoId) {
        return medicalInformationRepository.findById(medicalInfoId)
                .orElseThrow(() -> new RuntimeException("Medical Information not found for ID: " + medicalInfoId));
    }

    @Override
    public List<MedicalInformation> getAllMedicalInformation() {
        return medicalInformationRepository.findAll();
    }

    @Override
    public void deleteMedicalInformation(Long medicalInfoId) {
        medicalInformationRepository.deleteById(medicalInfoId);
    }
}
