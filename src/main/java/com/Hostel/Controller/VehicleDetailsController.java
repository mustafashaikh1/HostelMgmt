package com.Hostel.Controller;


import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.VehicleDetails;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Service.HostelFormService;
import com.Hostel.Service.VehicleDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
@RequestMapping("/vehicles")
public class VehicleDetailsController {

    @Autowired
    private VehicleDetailsService vehicleDetailsService;

    @Autowired
    private HostelFormService hostelFormService;


    @Autowired
    private HostelFormRepository hostelFormRepository;


    @PostMapping("/addVehicle")
    public HostelForm addVehicle(
            @RequestParam String formNumber,
            @RequestBody VehicleDetails vehicleDetails) {

        // Fetch the HostelForm by formNumber
        HostelForm hostelForm = hostelFormService.getHostelFormByFormNumber(formNumber);

        // If HostelForm does not exist, throw an exception
        if (hostelForm == null) {
            throw new RuntimeException("HostelForm not found with formNumber: " + formNumber);
        }



        // Set the HostelForm to the VehicleDetails object
        vehicleDetails.setHostelForm(hostelForm);

        // Save the VehicleDetails
        VehicleDetails savedVehicleDetails = vehicleDetailsService.saveVehicleDetails(vehicleDetails, formNumber);

        // Optionally, update the HostelForm with the saved VehicleDetails
        hostelForm.setVehicleDetails(savedVehicleDetails);
        hostelFormService.saveHostelForm(hostelForm);

        return hostelForm;
    }




    @GetMapping("/all")
    public ResponseEntity<List<VehicleDetails>> getAllVehicles() {
        return ResponseEntity.ok(vehicleDetailsService.getAllVehicles());
    }

    @GetMapping("/{regNumber}")
    public ResponseEntity<VehicleDetails> getVehicleByRegNumber(@PathVariable String regNumber) {
        return ResponseEntity.ok(vehicleDetailsService.getVehicleByRegNumber(regNumber));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VehicleDetails> updateVehicle(
            @PathVariable Long id, @RequestBody VehicleDetails updatedVehicle) {
        return ResponseEntity.ok(vehicleDetailsService.updateVehicleDetails(id, updatedVehicle));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long id) {
        vehicleDetailsService.deleteVehicle(id);
        return ResponseEntity.ok("Vehicle deleted successfully");
    }
}

