package com.Hostel.Repository;


import com.Hostel.Entity.HostelUrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostelUrlMappingRepository extends JpaRepository<HostelUrlMapping, Long> {

    Optional<HostelUrlMapping> findByDynamicPart(String dynamicPart);
}
