package com.Hostel.Service;


import com.Hostel.Entity.WorkDetails;

import java.util.Optional;

public interface WorkDetailsService {
    WorkDetails saveWorkDetails(WorkDetails workDetails);
    Optional<WorkDetails> getWorkDetailsById(Long workDetailsId);
    void deleteWorkDetails(Long workDetailsId);
}
