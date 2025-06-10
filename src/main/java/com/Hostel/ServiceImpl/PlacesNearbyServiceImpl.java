package com.Hostel.ServiceImpl;

import com.Hostel.Entity.PlacesNearby;
import com.Hostel.Repository.PlacesNearbyRepository;
import com.Hostel.Service.PlacesNearbyService;
import com.Hostel.Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PlacesNearbyServiceImpl implements PlacesNearbyService {

    @Autowired
    private PlacesNearbyRepository placesNearbyRepository;

    @Autowired
    private S3Service s3Service;

    @Override
    public PlacesNearby addPlace(PlacesNearby placesNearby, MultipartFile placeImage) throws IOException {
        if (placeImage != null && !placeImage.isEmpty()) {
            String imageUrl = s3Service.uploadImage(placeImage);
            placesNearby.setPlaceImageUrl(imageUrl);
        }
        return placesNearbyRepository.save(placesNearby);
    }

    @Override
    public List<PlacesNearby> getAllPlaces() {
        return placesNearbyRepository.findAll();
    }

    @Override
    public PlacesNearby getPlaceById(Long placesNearbyId) {
        return placesNearbyRepository.findById(placesNearbyId)
                .orElseThrow(() -> new RuntimeException("Place not found with ID: " + placesNearbyId));
    }

    @Override
    public List<PlacesNearby> getPlacesByCategory(String place) {
        return placesNearbyRepository.findByPlace(place);
    }

//    @Override
//    public PlacesNearby updatePlace(Long placesNearbyId, PlacesNearby placesNearby, MultipartFile placeImage) throws IOException {
//        PlacesNearby existingPlace = getPlaceById(placesNearbyId);
//        existingPlace.setPlace(placesNearby.getPlace());
//        existingPlace.setPlaceName(placesNearby.getPlaceName());
//        existingPlace.setDistance(placesNearby.getDistance());
//        existingPlace.setTravelTime(placesNearby.getTravelTime());
//        existingPlace.setDescription(placesNearby.getDescription());
//
//        if (placeImage != null && !placeImage.isEmpty()) {
//            String imageUrl = s3Service.uploadImage(placeImage);
//            existingPlace.setPlaceImageUrl(imageUrl);
//            System.out.println("Updated Image URL: " + imageUrl); // Debugging
//        }
//
//        return placesNearbyRepository.save(existingPlace);
//    }


    @Override
    public PlacesNearby updatePlace(Long placesNearbyId, PlacesNearby placesNearby, MultipartFile placeImage) throws IOException {
        PlacesNearby existingPlace = placesNearbyRepository.findById(placesNearbyId)
                .orElseThrow(() -> new RuntimeException("❌ Place not found with ID: " + placesNearbyId));

        // ✅ Update all text fields
        existingPlace.setPlace(placesNearby.getPlace());
        existingPlace.setPlaceName(placesNearby.getPlaceName());
        existingPlace.setDistance(placesNearby.getDistance());
        existingPlace.setTravelTime(placesNearby.getTravelTime());
        existingPlace.setDescription(placesNearby.getDescription());

        // ✅ Only update image if a new one is uploaded
        if (placeImage != null && !placeImage.isEmpty()) {
            try {
                String imageUrl = s3Service.uploadImage(placeImage);
                existingPlace.setPlaceImageUrl(imageUrl);
                System.out.println("✅ Updated Image URL: " + imageUrl);
            } catch (Exception e) {
                throw new IOException("❌ Error uploading new image: " + e.getMessage());
            }
        }

        return placesNearbyRepository.save(existingPlace);
    }



    @Override
    public void deletePlace(Long placesNearbyId) {
        PlacesNearby placesNearby = placesNearbyRepository.findById(placesNearbyId)
                .orElseThrow(() -> new RuntimeException("❌ Place not found with ID: " + placesNearbyId));

        // ❌ Do NOT delete image from S3
        placesNearbyRepository.delete(placesNearby);
    }

}
