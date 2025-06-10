package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelGallery;
import com.Hostel.Repository.HostelGalleryRepository;
import com.Hostel.Service.HostelGalleryService;
import com.Hostel.Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HostelGalleryServiceImpl implements HostelGalleryService {

    @Autowired
    private HostelGalleryRepository hostelGalleryRepository;

    @Autowired
    private S3Service s3Service;

    // Upload a single photo
    @Override
    public HostelGallery uploadSinglePhoto(MultipartFile file, String description) throws IOException {
        String imageUrl = s3Service.uploadImage(file);
        HostelGallery hostelGallery = new HostelGallery();
        hostelGallery.setImageUrl(imageUrl);
        hostelGallery.setDescription(description);
        return hostelGalleryRepository.save(hostelGallery);
    }

    // Upload multiple photos
    @Override
    public List<HostelGallery> uploadMultiplePhotos(List<MultipartFile> files, List<String> descriptions) throws IOException {
        List<HostelGallery> uploadedPhotos = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            String description = (i < descriptions.size()) ? descriptions.get(i) : "No Description";
            HostelGallery photo = uploadSinglePhoto(files.get(i), description);
            uploadedPhotos.add(photo);
        }
        return uploadedPhotos;
    }


    @Override
    public HostelGallery updatePhoto(Long id, MultipartFile file, String description) throws IOException {
        HostelGallery existingPhoto = hostelGalleryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with ID: " + id));

        // Update description
        existingPhoto.setDescription(description);

        // If a new file is provided, update the image URL without deleting the old one
        if (file != null && !file.isEmpty()) {
            // Upload new image to S3
            String newImageUrl = s3Service.uploadImage(file);
            existingPhoto.setImageUrl(newImageUrl);
        }

        return hostelGalleryRepository.save(existingPhoto);
    }




    @Override
    public List<HostelGallery> getAllPhotos() {
        return hostelGalleryRepository.findAll();
    }

    @Override
    public HostelGallery getPhotoById(Long id) {
        return hostelGalleryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with ID: " + id));
    }

    @Override
    public void deletePhoto(Long id) {
        HostelGallery photo = hostelGalleryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Photo not found with ID: " + id));



        // Delete only the database entry
        hostelGalleryRepository.delete(photo);
    }


}
