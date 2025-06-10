package com.Hostel.Service;

import com.Hostel.Entity.HostelManuBar;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HostelManuBarService {
    HostelManuBar createHostelManuBar(HostelManuBar hostelManuBar, MultipartFile hostelManubarImage) throws IOException;

    HostelManuBar getHostelManuBarById(Long id);

    List<HostelManuBar> getAllHostelManuBars();

    HostelManuBar updateHostelManuBar(Long id, String hostelManuBarColor, MultipartFile hostelManubarImage) throws IOException;
    void deleteHostelManuBar(Long id);
}
