package com.Hostel.Service;


import com.Hostel.Entity.OurFacilities;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OurFacilitiesService {
    OurFacilities addFacility(String facilityName, String description, MultipartFile facilityImage) throws IOException;
    List<OurFacilities> getAllFacilities();
    OurFacilities getFacilityById(Long facilityId);
    OurFacilities updateFacility(Long facilityId, String facilityName, String description, MultipartFile facilityImage) throws IOException;
    void deleteFacility(Long facilityId);
}
