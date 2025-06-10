package com.Hostel.ServiceImpl;

import com.Hostel.Entity.Admin;
import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.LeaveApplication;
import com.Hostel.Repository.AdminRepository;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Repository.LeaveApplicationRepository;
import com.Hostel.Service.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private HostelFormRepository hostelFormRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public LeaveApplication applyForLeave(LeaveApplication leaveApplication) {
        // Validate form number exists
        Optional<HostelForm> hostelForm = hostelFormRepository.findByFormNumber(leaveApplication.getFormNumber());
        if (hostelForm.isEmpty()) {
            throw new RuntimeException("Student with form number " + leaveApplication.getFormNumber() + " not found");
        }

        // Check if student has pending leave applications
        long pendingCount = leaveApplicationRepository.countPendingLeavesByFormNumber(leaveApplication.getFormNumber());
        if (pendingCount > 0) {
            throw new RuntimeException("You already have a pending leave application. Please wait for approval.");
        }

        // Check for overlapping approved leaves
        if (hasOverlappingApprovedLeaves(leaveApplication.getFormNumber(),
                leaveApplication.getStartDate(),
                leaveApplication.getEndDate())) {
            throw new RuntimeException("You already have approved leave for the selected dates");
        }

        // Set hostel form reference
        leaveApplication.setHostelForm(hostelForm.get());

        // Set student name from personal info
        String studentName = "Unknown Student";
        if (hostelForm.get().getPersonalInfo() != null &&
                hostelForm.get().getPersonalInfo().getFullName() != null &&
                !hostelForm.get().getPersonalInfo().getFullName().trim().isEmpty()) {
            studentName = hostelForm.get().getPersonalInfo().getFullName().trim();
        } else {
            // Fallback to form number if name is not available
            studentName = "Student-" + leaveApplication.getFormNumber();
        }
        leaveApplication.setStudentName(studentName);

        // Set default status and application date
        leaveApplication.setStatus(LeaveApplication.LeaveStatus.PENDING);
        leaveApplication.setApplicationDate(LocalDateTime.now());

        return leaveApplicationRepository.save(leaveApplication);
    }

    @Override
    public LeaveApplication approveLeave(Long leaveId, Long adminId, String adminRemarks) {
        Optional<LeaveApplication> leaveOpt = leaveApplicationRepository.findById(leaveId);
        if (leaveOpt.isEmpty()) {
            throw new RuntimeException("Leave application not found");
        }

        Optional<Admin> adminOpt = adminRepository.findById(adminId);
        if (adminOpt.isEmpty()) {
            throw new RuntimeException("Admin not found");
        }

        LeaveApplication leave = leaveOpt.get();
        leave.setStatus(LeaveApplication.LeaveStatus.APPROVED);
        leave.setAdminRemarks(adminRemarks);
        leave.setApprovalDate(LocalDateTime.now());
        leave.setApprovedByAdmin(adminOpt.get());

        return leaveApplicationRepository.save(leave);
    }

    @Override
    public LeaveApplication rejectLeave(Long leaveId, Long adminId, String adminRemarks) {
        Optional<LeaveApplication> leaveOpt = leaveApplicationRepository.findById(leaveId);
        if (leaveOpt.isEmpty()) {
            throw new RuntimeException("Leave application not found");
        }

        Optional<Admin> adminOpt = adminRepository.findById(adminId);
        if (adminOpt.isEmpty()) {
            throw new RuntimeException("Admin not found");
        }

        LeaveApplication leave = leaveOpt.get();
        leave.setStatus(LeaveApplication.LeaveStatus.REJECTED);
        leave.setAdminRemarks(adminRemarks);
        leave.setApprovalDate(LocalDateTime.now());
        leave.setApprovedByAdmin(adminOpt.get());

        return leaveApplicationRepository.save(leave);
    }

    @Override
    public List<LeaveApplication> getAllLeaveApplications() {
        return leaveApplicationRepository.findAllWithStudentDetails();
    }

    @Override
    public List<LeaveApplication> getLeaveApplicationsByStatus(LeaveApplication.LeaveStatus status) {
        return leaveApplicationRepository.findByStatusOrderByApplicationDateDesc(status);
    }

    @Override
    public List<LeaveApplication> getLeaveApplicationsByFormNumber(String formNumber) {
        return leaveApplicationRepository.findByFormNumberOrderByApplicationDateDesc(formNumber);
    }

    @Override
    public Optional<LeaveApplication> getLeaveApplicationById(Long leaveId) {
        return leaveApplicationRepository.findById(leaveId);
    }

    @Override
    public void deleteLeaveApplication(Long leaveId) {
        leaveApplicationRepository.deleteById(leaveId);
    }

    @Override
    public List<LeaveApplication> getPendingLeaveApplications() {
        return leaveApplicationRepository.findByStatusOrderByApplicationDateAsc(LeaveApplication.LeaveStatus.PENDING);
    }

    @Override
    public boolean hasOverlappingApprovedLeaves(String formNumber, java.time.LocalDate startDate, java.time.LocalDate endDate) {
        List<LeaveApplication> overlapping = leaveApplicationRepository.findOverlappingApprovedLeaves(formNumber, startDate, endDate);
        return !overlapping.isEmpty();
    }
}