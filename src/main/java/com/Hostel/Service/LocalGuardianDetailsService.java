package com.Hostel.Service;

import com.Hostel.Entity.LocalGuardianDetails;

import java.util.List;

public interface LocalGuardianDetailsService {
    LocalGuardianDetails saveLocalGuardianDetails(LocalGuardianDetails localGuardianDetails, String formNumber);

    LocalGuardianDetails updateLocalGuardianDetails(Long guardianId, LocalGuardianDetails localGuardianDetails);

    LocalGuardianDetails getLocalGuardianDetailsById(Long guardianId);

    List<LocalGuardianDetails> getAllLocalGuardianDetails();

    void deleteLocalGuardianDetails(Long guardianId);
}
