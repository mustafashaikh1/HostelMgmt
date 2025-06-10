package com.Hostel.Service;


import com.Hostel.Entity.HostelTestimonials;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HostelTestimonialsService {
    HostelTestimonials createTestimonial(HostelTestimonials testimonial, MultipartFile file) throws IOException;
    HostelTestimonials updateTestimonial(Long id, HostelTestimonials testimonial, MultipartFile file) throws IOException;
    void deleteTestimonialById(Long id);
    HostelTestimonials getTestimonialById(Long id);
    List<HostelTestimonials> getAllTestimonials();
}
