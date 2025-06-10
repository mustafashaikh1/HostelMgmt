package com.Hostel.Service;

import com.Hostel.Entity.HostelSlideBar;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface HostelSlideBarService {
    HostelSlideBar createHostelSlideBar(HostelSlideBar hostelSlideBar, List<MultipartFile> slideImages) throws IOException;

    HostelSlideBar updateHostelSlideBarImageById(Long slideBarId, MultipartFile newImage, String slideBarColor, Long imageId) throws IOException;    void deleteHostelSlideBar(Long id);

    Optional<HostelSlideBar> getHostelSlideBarById(Long id);
    List<HostelSlideBar> getAllHostelSlideBars();

    HostelSlideBar deleteSlideImageByImageId(Long slideBarId, Long imageId);
}
