package com.Hostel.Repository;



import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.StudyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyDetailsRepository extends JpaRepository<StudyDetails, Long> {



    Optional<StudyDetails> findByHostelForm(HostelForm hostelForm);
    // You can add custom queries here if needed
}
