package com.Hostel.ServiceImpl;

import com.Hostel.Entity.OurFacilities;
import com.Hostel.Repository.OurFacilitiesRepository;
import com.Hostel.Service.OurFacilitiesService;
import com.Hostel.Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class OurFacilitiesServiceImpl implements OurFacilitiesService {

    @Autowired
    private OurFacilitiesRepository ourFacilitiesRepository;

    @Autowired
    private S3Service s3Service;

    @Override
    public OurFacilities addFacility(String facilityName, String description, MultipartFile facilityImage) throws IOException {
        String imageUrl = (facilityImage != null) ? s3Service.uploadImage(facilityImage) : null;

        OurFacilities facility = new OurFacilities();
        facility.setFacilityName(facilityName);
        facility.setDescription(description);
        facility.setFacilityImageUrl(imageUrl);

        return ourFacilitiesRepository.save(facility);
    }

    @Override
    public List<OurFacilities> getAllFacilities() {
        return ourFacilitiesRepository.findAll();
    }

    @Override
    public OurFacilities getFacilityById(Long facilityId) {
        return ourFacilitiesRepository.findById(facilityId)
                .orElseThrow(() -> new RuntimeException("Facility not found with ID: " + facilityId));
    }

    @Override
    public OurFacilities updateFacility(Long facilityId, String facilityName, String description, MultipartFile facilityImage) throws IOException {
        OurFacilities facility = getFacilityById(facilityId);
        facility.setFacilityName(facilityName);
        facility.setDescription(description);

        if (facilityImage != null) {
            String imageUrl = s3Service.uploadImage(facilityImage);
            facility.setFacilityImageUrl(imageUrl);
        }

        return ourFacilitiesRepository.save(facility);
    }

    @Override
    public void deleteFacility(Long facilityId) {
        OurFacilities facility = getFacilityById(facilityId);
        ourFacilitiesRepository.delete(facility);
    }
}
