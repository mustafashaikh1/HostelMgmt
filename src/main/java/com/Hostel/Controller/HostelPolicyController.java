package com.Hostel.Controller;

import com.Hostel.Entity.HostelPolicy;
import com.Hostel.Service.HostelPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
@RequestMapping("/public")
public class HostelPolicyController {

    @Autowired
    private HostelPolicyService hostelPolicyService;

    @PostMapping("/createHostelPolicy")
    public ResponseEntity<HostelPolicy> createPolicy(@RequestBody HostelPolicy hostelPolicy) {
        HostelPolicy createdPolicy = hostelPolicyService.createPolicy(hostelPolicy);
        return ResponseEntity.ok(createdPolicy);
    }

    @GetMapping("/getAllHostelPolicies")
    public ResponseEntity<List<HostelPolicy>> getAllPolicies() {
        List<HostelPolicy> policies = hostelPolicyService.getAllPolicies();
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/getHostelPoliciesById/{id}")
    public ResponseEntity<HostelPolicy> getPolicyById(@PathVariable Integer id) {
        Optional<HostelPolicy> policy = hostelPolicyService.getPolicyById(id);
        return policy.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/updateHostelPoliciesById/{id}")
    public ResponseEntity<HostelPolicy> updatePolicy(@PathVariable Integer id, @RequestBody HostelPolicy hostelPolicy) {
        try {
            HostelPolicy updatedPolicy = hostelPolicyService.updatePolicy(id, hostelPolicy);
            return ResponseEntity.ok(updatedPolicy);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteHostelPoliciesById/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable Integer id) {
        try {
            hostelPolicyService.deletePolicy(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getHostelPolicyByType/{type}")
    public ResponseEntity<HostelPolicy> getPolicyByType(@PathVariable String type) {
        Optional<HostelPolicy> policy = hostelPolicyService.getPolicyByType(type);
        return policy.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
