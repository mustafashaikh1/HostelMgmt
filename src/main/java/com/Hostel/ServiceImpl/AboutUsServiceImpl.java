package com.Hostel.ServiceImpl;

import com.Hostel.Entity.AboutUs;
import com.Hostel.Repository.AboutUsRepository;
import com.Hostel.Service.AboutUsService;
import com.Hostel.Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AboutUsServiceImpl implements AboutUsService {

    @Autowired
    private AboutUsRepository aboutUsRepository;

    @Autowired
    private S3Service s3Service;

    @Override
    public AboutUs addAboutUs(AboutUs aboutUs, MultipartFile aboutUsImage) throws IOException {
        if (aboutUsImage != null && !aboutUsImage.isEmpty()) {
            String imageUrl = s3Service.uploadImage(aboutUsImage);
            aboutUs.setAboutUsImage(imageUrl);
        }
        return aboutUsRepository.save(aboutUs);
    }

    @Override
    public List<AboutUs> getAllAboutUs() {
        return aboutUsRepository.findAll();
    }

    @Override
    public AboutUs getAboutUsById(Long aboutUsId) {
        return aboutUsRepository.findById(aboutUsId)
                .orElseThrow(() -> new RuntimeException("About Us entry not found with ID: " + aboutUsId));
    }

    @Override
    public AboutUs updateAboutUs(Long aboutUsId, AboutUs aboutUs, MultipartFile aboutUsImage) throws IOException {
        AboutUs existingAboutUs = getAboutUsById(aboutUsId);

        // Update description
        existingAboutUs.setDescription(aboutUs.getDescription());

        // ✅ Only update the image if a new one is provided
        if (aboutUsImage != null && !aboutUsImage.isEmpty()) {
            String imageUrl = s3Service.uploadImage(aboutUsImage);

            // ✅ Append the new image URL instead of replacing the previous one
            if (existingAboutUs.getAboutUsImage() != null && !existingAboutUs.getAboutUsImage().isEmpty()) {
                existingAboutUs.setAboutUsImage(existingAboutUs.getAboutUsImage() + "," + imageUrl);
            } else {
                existingAboutUs.setAboutUsImage(imageUrl);
            }
        }

        return aboutUsRepository.save(existingAboutUs);
    }


    @Override
    public void deleteAboutUs(Long aboutUsId) {
        AboutUs aboutUs = getAboutUsById(aboutUsId);

        // ❌ Do NOT delete the image from S3, just remove the database entry
        aboutUsRepository.delete(aboutUs);
    }


}