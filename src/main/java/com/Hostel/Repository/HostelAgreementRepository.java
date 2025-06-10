package com.Hostel.Repository;

import com.Hostel.Entity.HostelAgreement;
import com.Hostel.Entity.HostelForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostelAgreementRepository extends JpaRepository<HostelAgreement, Long> {

    // Find HostelAgreement by form number
    HostelAgreement findByHostelForm_FormNumber(String formNumber);

    // Check if an agreement exists for a given form number (via HostelForm entity)
    boolean existsByHostelForm_FormNumber(String formNumber);

    // Delete a HostelAgreement by form number (via HostelForm entity)
    void deleteByHostelForm_FormNumber(String formNumber);

    Optional<HostelAgreement> findByHostelForm(HostelForm hostelForm);

}
