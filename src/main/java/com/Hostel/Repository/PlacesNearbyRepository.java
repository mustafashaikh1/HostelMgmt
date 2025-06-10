package com.Hostel.Repository;


import com.Hostel.Entity.PlacesNearby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlacesNearbyRepository extends JpaRepository<PlacesNearby, Long> {
    List<PlacesNearby> findByPlace(String place);
}
