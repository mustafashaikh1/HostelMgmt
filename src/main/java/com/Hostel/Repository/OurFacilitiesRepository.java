package com.Hostel.Repository;


import com.Hostel.Entity.OurFacilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OurFacilitiesRepository extends JpaRepository<OurFacilities, Long> {
}
