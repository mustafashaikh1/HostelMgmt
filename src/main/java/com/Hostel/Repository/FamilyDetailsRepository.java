package com.Hostel.Repository;

import com.Hostel.Entity.FamilyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyDetailsRepository extends JpaRepository<FamilyDetails, Long> {

    List<FamilyDetails> findByCity(String city);

}
