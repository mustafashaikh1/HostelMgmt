package com.Hostel.Repository;

import com.Hostel.Entity.HostelInquiryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostelInquiryStatusRepository extends JpaRepository<HostelInquiryStatus, Long> {

    // Retrieve all status updates for a specific inquiry
    List<HostelInquiryStatus> findByHostelInquiry_InquiryId(Long inquiryId);



}
