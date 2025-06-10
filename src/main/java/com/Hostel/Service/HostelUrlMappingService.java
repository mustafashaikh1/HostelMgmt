package com.Hostel.Service;


import com.Hostel.Entity.HostelUrlMapping;

import java.util.List;

public interface HostelUrlMappingService {
    HostelUrlMapping createHostelUrlMapping(String dynamicPart);
    HostelUrlMapping getHostelUrlMapping(String dynamicPart);
    List<HostelUrlMapping> getAllHostelUrlMappings();
    HostelUrlMapping updateHostelUrlMapping(Long id, String dynamicPart);
    void deleteHostelUrlMapping(Long id);
}
