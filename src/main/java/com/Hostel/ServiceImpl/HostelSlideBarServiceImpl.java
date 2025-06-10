package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelSlideBar;
import com.Hostel.Repository.HostelSlideBarRepository;
import com.Hostel.Service.HostelSlideBarService;
import com.Hostel.Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class HostelSlideBarServiceImpl implements HostelSlideBarService {

    @Autowired
    private HostelSlideBarRepository hostelSlideBarRepository;

    @Autowired
    private S3Service s3Service; // Assuming you're using S3 for image storage

    @Override
    public HostelSlideBar createHostelSlideBar(HostelSlideBar hostelSlideBar, List<MultipartFile> slideImages) throws IOException {
        if (hostelSlideBar.getSlideImages() == null) {
            hostelSlideBar.setSlideImages(new ArrayList<>());
        }
        if (hostelSlideBar.getImageIds() == null) {
            hostelSlideBar.setImageIds(new ArrayList<>());
        }

        int imageIdCounter = 1;
        if (slideImages != null && !slideImages.isEmpty()) {
            for (MultipartFile slideImage : slideImages) {
                String imageUrl = s3Service.uploadImage(slideImage);
                hostelSlideBar.getSlideImages().add(imageUrl);
                hostelSlideBar.getImageIds().add(imageIdCounter++);
            }
        }

        return hostelSlideBarRepository.save(hostelSlideBar);
    }

    @Override
    public HostelSlideBar updateHostelSlideBarImageById(Long slideBarId, MultipartFile newImage, String slideBarColor, Long imageId) throws IOException {
        HostelSlideBar hostelSlideBar = hostelSlideBarRepository.findById(slideBarId)
                .orElseThrow(() -> new RuntimeException("HostelSlideBar not found with ID: " + slideBarId));

        if (slideBarColor != null && !slideBarColor.trim().isEmpty()) {
            hostelSlideBar.setSlideBarColor(slideBarColor);
        }

        // Upload image to S3
        String newImageUrl = s3Service.uploadImage(newImage);

        if (hostelSlideBar.getSlideImages() == null) {
            hostelSlideBar.setSlideImages(new ArrayList<>());
        }
        if (hostelSlideBar.getImageIds() == null) {
            hostelSlideBar.setImageIds(new ArrayList<>());
        }

        if (imageId != null) {
            // Update existing image
            int index = hostelSlideBar.getImageIds().indexOf(imageId.intValue());
            if (index != -1) {
                hostelSlideBar.getSlideImages().set(index, newImageUrl);
            } else {
                // If ID not found, treat as new image
                hostelSlideBar.getSlideImages().add(newImageUrl);
                hostelSlideBar.getImageIds().add(imageId.intValue());
            }
        } else {
            // Append new image with a new ID
            int newId = hostelSlideBar.getImageIds().isEmpty() ? 1
                    : Collections.max(hostelSlideBar.getImageIds()) + 1;
            hostelSlideBar.getSlideImages().add(newImageUrl);
            hostelSlideBar.getImageIds().add(newId);
        }

        return hostelSlideBarRepository.save(hostelSlideBar);
    }






    @Override
    public void deleteHostelSlideBar(Long id) {
        Optional<HostelSlideBar> optionalSlideBar = hostelSlideBarRepository.findById(id);

        if (optionalSlideBar.isPresent()) {
            // ✅ Simply delete the HostelSlideBar from the database (images in S3 remain untouched)
            hostelSlideBarRepository.deleteById(id);
        } else {
            throw new RuntimeException("HostelSlideBar not found with ID: " + id);
        }
    }


    @Override
    public Optional<HostelSlideBar> getHostelSlideBarById(Long id) {
        return hostelSlideBarRepository.findById(id);
    }

    @Override
    public List<HostelSlideBar> getAllHostelSlideBars() {
        return hostelSlideBarRepository.findAll();
    }


    @Override
    public HostelSlideBar deleteSlideImageByImageId(Long slideBarId, Long imageId) {
        Optional<HostelSlideBar> optionalSlideBar = hostelSlideBarRepository.findById(slideBarId);

        if (optionalSlideBar.isEmpty()) {
            throw new RuntimeException("HostelSlideBar not found with ID: " + slideBarId);
        }

        HostelSlideBar hostelSlideBar = optionalSlideBar.get();

        List<Integer> imageIds = hostelSlideBar.getImageIds();
        List<String> slideImages = hostelSlideBar.getSlideImages();

        if (imageIds == null || slideImages == null) {
            throw new RuntimeException("No images found in HostelSlideBar with ID: " + slideBarId);
        }

        int index = imageIds.indexOf(imageId.intValue());
        if (index == -1) {
            throw new RuntimeException("Image ID " + imageId + " not found in HostelSlideBar " + slideBarId);
        }

        // Optionally: delete image from S3 (if needed)
        // String imageUrl = slideImages.get(index);
        // s3Service.deleteImage(imageUrl);

        // Remove image and ID
        imageIds.remove(index);
        slideImages.remove(index);

        return hostelSlideBarRepository.save(hostelSlideBar);
    }

}
