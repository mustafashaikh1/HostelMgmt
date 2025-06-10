package com.Hostel.Repository;


import com.Hostel.Entity.UndertakingForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UndertakingFormRepository extends JpaRepository<UndertakingForm, Long> {

    // Find UndertakingForm by HostelForm's formNumber
    Optional<UndertakingForm> findByHostelForm_FormNumber(String formNumber);
}
