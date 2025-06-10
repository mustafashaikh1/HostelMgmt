package com.Hostel.Repository;

import com.Hostel.Entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {
    Optional<Floor> findByFloorName(String floorName);
    List<Floor> findByHostelBuildingNameIgnoreCase(String hostelBuildingName);

}
