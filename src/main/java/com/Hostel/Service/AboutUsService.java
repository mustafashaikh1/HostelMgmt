package com.Hostel.Service;


import com.Hostel.Entity.AboutUs;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AboutUsService {
    AboutUs addAboutUs(AboutUs aboutUs, MultipartFile aboutUsImage) throws IOException;
    List<AboutUs> getAllAboutUs();
    AboutUs getAboutUsById(Long aboutUsId);
    AboutUs updateAboutUs(Long aboutUsId, AboutUs aboutUs, MultipartFile aboutUsImage) throws IOException;
    void deleteAboutUs(Long aboutUsId);
}