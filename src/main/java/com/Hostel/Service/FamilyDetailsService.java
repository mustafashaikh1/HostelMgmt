package com.Hostel.Service;

import com.Hostel.Entity.FamilyDetails;

import java.util.List;

public interface FamilyDetailsService {

    FamilyDetails saveFamilyDetails(FamilyDetails familyDetails, String formNumber);


    List<FamilyDetails> getAllFamilyDetails();

    FamilyDetails getFamilyDetailsById(Long familyDetailsId);  // Updated method signature

    List<FamilyDetails> getFamilyDetailsByCity(String city);

    FamilyDetails updateFamilyDetails(Long familyDetailsId, FamilyDetails familyDetails);  // Updated method signature

    void deleteFamilyDetails(Long familyDetailsId);  // Updated method signature
}
