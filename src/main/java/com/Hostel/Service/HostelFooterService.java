package com.Hostel.Service;



import com.Hostel.Entity.HostelFooter;

import java.util.List;
import java.util.Optional;

public interface HostelFooterService {
    HostelFooter saveFooter(HostelFooter footer);
    HostelFooter updateFooter(Long footerId, HostelFooter updatedFooter);
    void deleteFooter(Long footerId);
    Optional<HostelFooter> getFooterById(Long footerId);
    List<HostelFooter> getAllFooters();
}
