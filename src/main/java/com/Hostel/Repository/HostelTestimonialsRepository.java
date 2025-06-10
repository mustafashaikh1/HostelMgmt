package com.Hostel.Repository;

import com.Hostel.Entity.HostelTestimonials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostelTestimonialsRepository extends JpaRepository<HostelTestimonials, Long> {
}
