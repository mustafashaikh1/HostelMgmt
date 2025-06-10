package com.Hostel.Repository;

import com.Hostel.Entity.HostelAssets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostelAssetsRepository extends JpaRepository<HostelAssets, Long> {
    boolean existsByModelNumber(String modelNumber);

}
