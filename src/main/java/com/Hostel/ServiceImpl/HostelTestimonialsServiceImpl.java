package com.Hostel.ServiceImpl;


import com.Hostel.Entity.HostelTestimonials;
import com.Hostel.Repository.HostelTestimonialsRepository;
import com.Hostel.Service.HostelTestimonialsService;
import com.Hostel.Service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HostelTestimonialsServiceImpl implements HostelTestimonialsService {

    @Autowired
    private HostelTestimonialsRepository testimonialsRepository;

    @Autowired
    private S3Service s3Service;

    @Override
    public HostelTestimonials createTestimonial(HostelTestimonials testimonial, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            testimonial.setTestimonialImage(s3Service.uploadImage(file));
        }
        return testimonialsRepository.save(testimonial);
    }

    @Override
    public HostelTestimonials updateTestimonial(Long id, HostelTestimonials testimonial, MultipartFile file) throws IOException {
        Optional<HostelTestimonials> existingTestimonialOpt = testimonialsRepository.findById(id);
        if (existingTestimonialOpt.isPresent()) {
            HostelTestimonials existingTestimonial = existingTestimonialOpt.get();

            existingTestimonial.setTestimonialTitle(testimonial.getTestimonialTitle());
            existingTestimonial.setTestimonialName(testimonial.getTestimonialName());
            existingTestimonial.setDescription(testimonial.getDescription());
            existingTestimonial.setTestimonialColor(testimonial.getTestimonialColor());

            if (file != null && !file.isEmpty()) {
                existingTestimonial.setTestimonialImage(s3Service.uploadImage(file));
            }

            return testimonialsRepository.save(existingTestimonial);
        } else {
            throw new RuntimeException("Testimonial not found with ID: " + id);
        }
    }

    @Override
    public void deleteTestimonialById(Long id) {
        if (!testimonialsRepository.existsById(id)) {
            throw new RuntimeException("Testimonial not found with ID: " + id);
        }
        testimonialsRepository.deleteById(id);
        log.info("Deleted testimonial with ID: {}", id);
    }

    @Override
    public HostelTestimonials getTestimonialById(Long id) {
        return testimonialsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Testimonial not found with ID: " + id));
    }

    @Override
    public List<HostelTestimonials> getAllTestimonials() {
        return testimonialsRepository.findAll();
    }
}

