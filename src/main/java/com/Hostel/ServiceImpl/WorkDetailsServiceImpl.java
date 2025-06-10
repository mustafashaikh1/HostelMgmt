package com.Hostel.ServiceImpl;

import com.Hostel.Entity.WorkDetails;
import com.Hostel.Repository.WorkDetailsRepository;
import com.Hostel.Service.WorkDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkDetailsServiceImpl implements WorkDetailsService {

    @Autowired
    private WorkDetailsRepository workDetailsRepository;

    @Override
    public WorkDetails saveWorkDetails(WorkDetails workDetails) {
        return workDetailsRepository.save(workDetails);
    }

    @Override
    public Optional<WorkDetails> getWorkDetailsById(Long workDetailsId) {
        return workDetailsRepository.findById(workDetailsId);
    }

    @Override
    public void deleteWorkDetails(Long workDetailsId) {
        workDetailsRepository.deleteById(workDetailsId);
    }
}
