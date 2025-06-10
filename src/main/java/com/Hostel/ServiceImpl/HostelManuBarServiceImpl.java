package com.Hostel.ServiceImpl;


import com.Hostel.Entity.HostelManuBar;
import com.Hostel.Repository.HostelManuBarRepository;
import com.Hostel.Service.HostelManuBarService;
import com.Hostel.Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class HostelManuBarServiceImpl implements HostelManuBarService {

    @Autowired
    private HostelManuBarRepository hostelManuBarRepository;

    @Autowired
    private S3Service s3Service; // If you're using AWS S3 for image storage

    @Override
    public HostelManuBar createHostelManuBar(HostelManuBar hostelManuBar, MultipartFile hostelManubarImage) throws IOException {
        if (hostelManubarImage != null && !hostelManubarImage.isEmpty()) {
            String imageUrl = s3Service.uploadImage(hostelManubarImage);
            hostelManuBar.setHostelManubarImage(imageUrl);
        }
        return hostelManuBarRepository.save(hostelManuBar);
    }

    @Override
    public HostelManuBar getHostelManuBarById(Long id) {
        return hostelManuBarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ HostelManuBar not found with ID: " + id));
    }

    @Override
    public List<HostelManuBar> getAllHostelManuBars() {
        return hostelManuBarRepository.findAll();
    }

    @Override
    public HostelManuBar updateHostelManuBar(Long id, String hostelManuBarColor, MultipartFile hostelManubarImage) throws IOException {
        HostelManuBar existingHostelManuBar = hostelManuBarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ HostelManuBar not found with ID: " + id));

        // Update color
        existingHostelManuBar.setHostelManuBarColor(hostelManuBarColor);

        // Update image only if a new one is provided
        if (hostelManubarImage != null && !hostelManubarImage.isEmpty()) {
            String imageUrl = s3Service.uploadImage(hostelManubarImage);
            existingHostelManuBar.setHostelManubarImage(imageUrl);
        }

        return hostelManuBarRepository.save(existingHostelManuBar);
    }


    @Override
    public void deleteHostelManuBar(Long id) {
        HostelManuBar hostelManuBar = getHostelManuBarById(id);
        hostelManuBarRepository.delete(hostelManuBar);
        // ❌ Image remains in S3 (not deleted)
    }
}
