package com.Hostel.Service;


import com.Hostel.Entity.PlacesNearby;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PlacesNearbyService {
    PlacesNearby addPlace(PlacesNearby placesNearby, MultipartFile placeImage) throws IOException;
    List<PlacesNearby> getAllPlaces();
    PlacesNearby getPlaceById(Long placesNearbyId);
    List<PlacesNearby> getPlacesByCategory(String place);
    PlacesNearby updatePlace(Long placesNearbyId, PlacesNearby placesNearby, MultipartFile placeImage) throws IOException;
    void deletePlace(Long placesNearbyId);
}
