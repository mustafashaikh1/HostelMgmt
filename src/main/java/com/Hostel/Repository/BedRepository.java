package com.Hostel.Repository;

import com.Hostel.Entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {

    List<Bed> findByRoomRoomId(Long roomId);
    List<Bed> findByHostelFormFormNumber(String formNumber);

    // ✅ Get all beds allocated (allocated = true)
    List<Bed> findByAllocatedTrue();

    // ✅ Get all beds not allocated (allocated = false)
    List<Bed> findByAllocatedFalse();

    Optional<Bed> findByBedNumber(String bedNumber);

}
