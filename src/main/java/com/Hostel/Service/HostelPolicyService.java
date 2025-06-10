package com.Hostel.Service;

import com.Hostel.Entity.HostelPolicy;

import java.util.List;
import java.util.Optional;

public interface HostelPolicyService {

    HostelPolicy createPolicy(HostelPolicy hostelPolicy);

    List<HostelPolicy> getAllPolicies();

    Optional<HostelPolicy> getPolicyById(Integer id);

    HostelPolicy updatePolicy(Integer id, HostelPolicy hostelPolicy);

    void deletePolicy(Integer id);
    Optional<HostelPolicy> getPolicyByType(String type);

}
