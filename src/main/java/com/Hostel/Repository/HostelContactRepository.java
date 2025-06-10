package com.Hostel.Repository;

import com.Hostel.Entity.HostelContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostelContactRepository extends JpaRepository<HostelContact, Long> {
}
