package com.Hostel.Controller;

import com.Hostel.Entity.LeaveApplication;
import com.Hostel.Service.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class LeaveApplicationController {

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    // Apply for leave
    @PostMapping("/public/applyForLeave")
    public ResponseEntity<?> applyForLeave(@RequestBody LeaveApplication leaveApplication) {
        try {
            LeaveApplication savedLeave = leaveApplicationService.applyForLeave(leaveApplication);
            return ResponseEntity.ok(savedLeave);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    // Approve leave application (Admin)
    @PutMapping("/approve/{leaveId}")
    public ResponseEntity<?> approveLeave(@PathVariable Long leaveId, @RequestBody Map<String, Object> request) {
        try {
            Long adminId = Long.valueOf(request.get("adminId").toString());
            String adminRemarks = request.getOrDefault("adminRemarks", "").toString();

            LeaveApplication approvedLeave = leaveApplicationService.approveLeave(leaveId, adminId, adminRemarks);
            return ResponseEntity.ok(approvedLeave);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    // Reject leave application (Admin)
    @PutMapping("/reject/{leaveId}")
    public ResponseEntity<?> rejectLeave(@PathVariable Long leaveId, @RequestBody Map<String, Object> request) {
        try {
            Long adminId = Long.valueOf(request.get("adminId").toString());
            String adminRemarks = request.getOrDefault("adminRemarks", "Rejected").toString();

            LeaveApplication rejectedLeave = leaveApplicationService.rejectLeave(leaveId, adminId, adminRemarks);
            return ResponseEntity.ok(rejectedLeave);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get all leave applications (Admin)
    @GetMapping("/getAllLeaveApplications")
    public ResponseEntity<List<LeaveApplication>> getAllLeaveApplications() {
        List<LeaveApplication> leaves = leaveApplicationService.getAllLeaveApplications();
        return ResponseEntity.ok(leaves);
    }

    // Get pending leave applications (Admin)
    @GetMapping("/getPendingLeaveApplications")
    public ResponseEntity<List<LeaveApplication>> getPendingLeaveApplications() {
        List<LeaveApplication> pendingLeaves = leaveApplicationService.getPendingLeaveApplications();
        return ResponseEntity.ok(pendingLeaves);
    }

    // Get leave applications by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeaveApplication>> getLeaveApplicationsByStatus(@PathVariable String status) {
        try {
            LeaveApplication.LeaveStatus leaveStatus = LeaveApplication.LeaveStatus.valueOf(status.toUpperCase());
            List<LeaveApplication> leaves = leaveApplicationService.getLeaveApplicationsByStatus(leaveStatus);
            return ResponseEntity.ok(leaves);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get leave applications by form number (Student)
    @GetMapping("/student/{formNumber}")
    public ResponseEntity<List<LeaveApplication>> getLeaveApplicationsByFormNumber(@PathVariable String formNumber) {
        List<LeaveApplication> leaves = leaveApplicationService.getLeaveApplicationsByFormNumber(formNumber);
        return ResponseEntity.ok(leaves);
    }

    // Get leave application by ID
    @GetMapping("/{leaveId}")
    public ResponseEntity<LeaveApplication> getLeaveApplicationById(@PathVariable Long leaveId) {
        Optional<LeaveApplication> leave = leaveApplicationService.getLeaveApplicationById(leaveId);
        return leave.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }




    // Delete leave application
    @DeleteMapping("/{leaveId}")
    public ResponseEntity<?> deleteLeaveApplication(@PathVariable Long leaveId) {
        try {
            leaveApplicationService.deleteLeaveApplication(leaveId);
            return ResponseEntity.ok(Map.of("message", "Leave application deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}