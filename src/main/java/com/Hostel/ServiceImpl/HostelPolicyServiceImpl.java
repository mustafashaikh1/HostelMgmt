package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelPolicy;
import com.Hostel.Repository.HostelPolicyRepository;
import com.Hostel.Service.HostelPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HostelPolicyServiceImpl implements HostelPolicyService {

    @Autowired
    private HostelPolicyRepository hostelPolicyRepository;

    @Override
    public HostelPolicy createPolicy(HostelPolicy hostelPolicy) {
        return hostelPolicyRepository.save(hostelPolicy);
    }

    @Override
    public List<HostelPolicy> getAllPolicies() {
        return hostelPolicyRepository.findAll();
    }

    @Override
    public Optional<HostelPolicy> getPolicyById(Integer id) {
        return hostelPolicyRepository.findById(id);
    }

    @Override
    public HostelPolicy updatePolicy(Integer id, HostelPolicy hostelPolicy) {
        if (hostelPolicyRepository.existsById(id)) {
            hostelPolicy.setId(id);
            return hostelPolicyRepository.save(hostelPolicy);
        } else {
            throw new RuntimeException("Policy not found");
        }
    }

    @Override
    public void deletePolicy(Integer id) {
        if (hostelPolicyRepository.existsById(id)) {
            hostelPolicyRepository.deleteById(id);
        } else {
            throw new RuntimeException("Policy not found");
        }
    }

    @Override
    public Optional<HostelPolicy> getPolicyByType(String type) {
        return hostelPolicyRepository.findByType(type);
    }

}
