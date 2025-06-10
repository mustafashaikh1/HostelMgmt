package com.Hostel.Repository;



import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.VehicleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleDetailsRepository extends JpaRepository<VehicleDetails, Long> {
    VehicleDetails findByRegistrationNumber(String registrationNumber);

    VehicleDetails findByHostelForm(HostelForm hostelForm);
}
