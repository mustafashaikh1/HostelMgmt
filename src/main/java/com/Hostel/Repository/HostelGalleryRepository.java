package com.Hostel.Repository;


import com.Hostel.Entity.HostelGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostelGalleryRepository extends JpaRepository<HostelGallery, Long> {
}

