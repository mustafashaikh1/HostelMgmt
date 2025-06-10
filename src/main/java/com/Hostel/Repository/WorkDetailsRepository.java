package com.Hostel.Repository;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.WorkDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkDetailsRepository extends JpaRepository<WorkDetails, Long> {

    Optional<WorkDetails> findByHostelForm(HostelForm hostelForm);
}
