package com.Hostel.Repository;

import com.Hostel.Entity.LocalGuardianDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalGuardianDetailsRepository extends JpaRepository<LocalGuardianDetails, Long> {
    // No methods related to instituteCode anymore
}
