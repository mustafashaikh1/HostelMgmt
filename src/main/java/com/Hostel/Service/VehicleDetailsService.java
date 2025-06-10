package com.Hostel.Service;

import com.Hostel.Entity.VehicleDetails;

import java.util.List;

public interface VehicleDetailsService {
    VehicleDetails saveVehicleDetails(VehicleDetails vehicleDetails,String formNumber);
    List<VehicleDetails> getAllVehicles();
    VehicleDetails getVehicleByRegNumber(String registrationNumber);
    VehicleDetails updateVehicleDetails(Long id, VehicleDetails updatedVehicle);
    void deleteVehicle(Long id);
}
