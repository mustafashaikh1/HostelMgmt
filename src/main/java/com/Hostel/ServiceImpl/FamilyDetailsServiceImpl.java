package com.Hostel.ServiceImpl;

import com.Hostel.Entity.FamilyDetails;
import com.Hostel.Entity.HostelForm;
import com.Hostel.Repository.FamilyDetailsRepository;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Service.FamilyDetailsService;
import com.Hostel.Service.HostelFormService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FamilyDetailsServiceImpl implements FamilyDetailsService {

    @Autowired
    private FamilyDetailsRepository familyDetailsRepository;

    @Autowired
    private HostelFormService hostelFormService;

    @Autowired
    private HostelFormRepository hostelFormRepository;

    @Override
    public FamilyDetails saveFamilyDetails(FamilyDetails familyDetails, String formNumber) {
        HostelForm hostelForm = hostelFormService.getHostelFormByFormNumber(formNumber);

        if (hostelForm == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HostelForm not found with formNumber: " + formNumber);
        }

        // Initialize lazy-loaded fields manually to avoid lazy loading issues
        Hibernate.initialize(hostelForm.getPersonalInfo());
        Hibernate.initialize(hostelForm.getFamilyDetails());

        // Set the HostelForm to the FamilyDetails object
        familyDetails.setHostelForm(hostelForm);

        // Save the FamilyDetails
        FamilyDetails savedFamilyDetails = familyDetailsRepository.save(familyDetails);

        // Update the HostelForm with the saved FamilyDetails
        hostelForm.setFamilyDetails(savedFamilyDetails);
        hostelFormService.saveHostelForm(hostelForm);

        return savedFamilyDetails;
    }

    @Override
    public List<FamilyDetails> getAllFamilyDetails() {
        List<FamilyDetails> familyDetailsList = familyDetailsRepository.findAll();
        if (familyDetailsList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No Family Details found.");
        }
        return familyDetailsList;
    }

    @Override
    public FamilyDetails getFamilyDetailsById(Long familyDetailsId) {
        return familyDetailsRepository.findById(familyDetailsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Family Details not found with ID: " + familyDetailsId));
    }

    @Override
    public List<FamilyDetails> getFamilyDetailsByCity(String city) {
        List<FamilyDetails> familyDetailsList = familyDetailsRepository.findByCity(city);
        if (familyDetailsList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Family Details found in city: " + city);
        }
        return familyDetailsList;
    }

    @Override
    public FamilyDetails updateFamilyDetails(Long familyDetailsId, FamilyDetails familyDetails) {
        FamilyDetails existingDetails = getFamilyDetailsById(familyDetailsId);

        existingDetails.setFatherFullName(familyDetails.getFatherFullName());
        existingDetails.setMotherSchoolName(familyDetails.getMotherSchoolName());
        existingDetails.setPermanentAddress(familyDetails.getPermanentAddress());
        existingDetails.setCity(familyDetails.getCity());
        existingDetails.setState(familyDetails.getState());
        existingDetails.setPincode(familyDetails.getPincode());
        existingDetails.setFatherMobileNo(familyDetails.getFatherMobileNo());
        existingDetails.setMotherMobileNo(familyDetails.getMotherMobileNo());

        return familyDetailsRepository.save(existingDetails);
    }

    @Override
    public void deleteFamilyDetails(Long familyDetailsId) {
        if (!familyDetailsRepository.existsById(familyDetailsId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Family Details not found with ID: " + familyDetailsId);
        }
        familyDetailsRepository.deleteById(familyDetailsId);
    }
}
