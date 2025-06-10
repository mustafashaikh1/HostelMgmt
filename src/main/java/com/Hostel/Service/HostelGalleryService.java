package com.Hostel.Service;


import com.Hostel.Entity.HostelGallery;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HostelGalleryService {
    HostelGallery uploadSinglePhoto(MultipartFile file, String description) throws IOException;
    List<HostelGallery> uploadMultiplePhotos(List<MultipartFile> files, List<String> descriptions) throws IOException;
    HostelGallery updatePhoto(Long id, MultipartFile file, String description) throws IOException;
    List<HostelGallery> getAllPhotos();
    HostelGallery getPhotoById(Long id);
    void deletePhoto(Long id);
}
