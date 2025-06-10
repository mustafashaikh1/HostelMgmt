package com.Hostel.Repository;

import com.Hostel.Entity.HostelPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HostelPolicyRepository extends JpaRepository<HostelPolicy, Integer> {
    Optional<HostelPolicy> findByType(String type);

}
