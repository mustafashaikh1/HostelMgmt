package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.LocalGuardianDetails;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Repository.LocalGuardianDetailsRepository;
import com.Hostel.Service.HostelFormService;
import com.Hostel.Service.LocalGuardianDetailsService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocalGuardianDetailsServiceImpl implements LocalGuardianDetailsService {

    @Autowired
    private LocalGuardianDetailsRepository localGuardianDetailsRepository;


    @Autowired
    private HostelFormService hostelFormService;

    @Autowired
    private HostelFormRepository hostelFormRepository;




    @Override
    public LocalGuardianDetails saveLocalGuardianDetails(LocalGuardianDetails localGuardianDetails, String formNumber) {
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
        localGuardianDetails.setHostelForm(hostelForm);

        // Save the LocalGuardianDetails directly using the repository
        LocalGuardianDetails savedGuardianDetails = localGuardianDetailsRepository.save(localGuardianDetails);

        // Optionally, update the HostelForm with the saved LocalGuardianDetails if required
        hostelForm.setLocalGuardianDetails(savedGuardianDetails); // Set the LocalGuardianDetails
        hostelFormService.saveHostelForm(hostelForm); // Save the updated HostelForm

        // Return the saved LocalGuardianDetails object
        return savedGuardianDetails;
    }



    @Override
    public LocalGuardianDetails updateLocalGuardianDetails(Long guardianId, LocalGuardianDetails localGuardianDetails) {
        Optional<LocalGuardianDetails> existingDetails = localGuardianDetailsRepository.findById(guardianId);
        if (existingDetails.isPresent()) {
            LocalGuardianDetails updatedDetails = existingDetails.get();
            updatedDetails.setGuardianFullName(localGuardianDetails.getGuardianFullName());
            updatedDetails.setGuardianPermanentAddress(localGuardianDetails.getGuardianPermanentAddress());
            updatedDetails.setCity(localGuardianDetails.getCity());
            updatedDetails.setState(localGuardianDetails.getState());
            updatedDetails.setPincode(localGuardianDetails.getPincode());
            updatedDetails.setMobileNo(localGuardianDetails.getMobileNo());
            updatedDetails.setAlternateMobileNo(localGuardianDetails.getAlternateMobileNo());
            updatedDetails.setHostelForm(localGuardianDetails.getHostelForm());
            return localGuardianDetailsRepository.save(updatedDetails);
        }
        throw new RuntimeException("Local Guardian Details not found with id: " + guardianId);
    }

    @Override
    public LocalGuardianDetails getLocalGuardianDetailsById(Long guardianId) {
        return localGuardianDetailsRepository.findById(guardianId).orElseThrow(() ->
                new RuntimeException("Local Guardian Details not found with id: " + guardianId));
    }

    @Override
    public List<LocalGuardianDetails> getAllLocalGuardianDetails() {
        return localGuardianDetailsRepository.findAll();
    }

    @Override
    public void deleteLocalGuardianDetails(Long guardianId) {
        if (localGuardianDetailsRepository.existsById(guardianId)) {
            localGuardianDetailsRepository.deleteById(guardianId);
        } else {
            throw new RuntimeException("Local Guardian Details not found with id: " + guardianId);
        }
    }
}
