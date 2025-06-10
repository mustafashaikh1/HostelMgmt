package com.Hostel.Repository;

import com.Hostel.Entity.Reception;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceptionRepository extends JpaRepository<Reception, Long> {

    Reception findByEmail(String email); // Custom query to find a Reception by email

    List<Reception> findByAdmin_Email(String adminEmail); // Fetch receptions assigned to a specific Admin
}
