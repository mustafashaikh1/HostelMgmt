package com.Hostel.ServiceImpl;


import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.VehicleDetails;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Repository.VehicleDetailsRepository;
import com.Hostel.Service.HostelFormService;
import com.Hostel.Service.VehicleDetailsService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleDetailsServiceImpl implements VehicleDetailsService {

    @Autowired
    private VehicleDetailsRepository vehicleDetailsRepository;

    @Autowired
    private HostelFormService hostelFormService;

    @Autowired
    private HostelFormRepository hostelFormRepository;



    @Override
    public VehicleDetails saveVehicleDetails(VehicleDetails vehicleDetails,String formNumber) {
        // Fetch the HostelForm by formNumber
        HostelForm hostelForm = hostelFormService.getHostelFormByFormNumber(formNumber);

        // If HostelForm does not exist, throw an exception
        if (hostelForm == null) {
            throw new RuntimeException("HostelForm not found with formNumber: " + formNumber);
        }

        // Initialize lazy-loaded fields manually to avoid lazy loading issues
        Hibernate.initialize(hostelForm.getPersonalInfo());
        Hibernate.initialize(hostelForm.getFamilyDetails());


        vehicleDetails.setHostelForm(hostelForm);


        VehicleDetails savedVehicleDetails = vehicleDetailsRepository.save(vehicleDetails);


        hostelForm.setVehicleDetails(savedVehicleDetails);
        hostelFormService.saveHostelForm(hostelForm);


        return savedVehicleDetails;

    }

    @Override
    public List<VehicleDetails> getAllVehicles() {
        return vehicleDetailsRepository.findAll();
    }

    @Override
    public VehicleDetails getVehicleByRegNumber(String registrationNumber) {
        return vehicleDetailsRepository.findByRegistrationNumber(registrationNumber);
    }

    @Override
    public VehicleDetails updateVehicleDetails(Long id, VehicleDetails updatedVehicle) {
        VehicleDetails existingVehicle = vehicleDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + id));

        existingVehicle.setVehicleTypeBrand(updatedVehicle.getVehicleTypeBrand());
        existingVehicle.setRegistrationNumber(updatedVehicle.getRegistrationNumber());
        existingVehicle.setIsParkedOnPremises(updatedVehicle.getIsParkedOnPremises());

        return vehicleDetailsRepository.save(existingVehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        vehicleDetailsRepository.deleteById(id);
    }
}
