package com.Hostel.Service;

import com.Hostel.Entity.LeaveApplication;

import java.util.List;
import java.util.Optional;

public interface LeaveApplicationService {

    LeaveApplication applyForLeave(LeaveApplication leaveApplication);

    LeaveApplication approveLeave(Long leaveId, Long adminId, String adminRemarks);

    LeaveApplication rejectLeave(Long leaveId, Long adminId, String adminRemarks);

    List<LeaveApplication> getAllLeaveApplications();

    List<LeaveApplication> getLeaveApplicationsByStatus(LeaveApplication.LeaveStatus status);

    List<LeaveApplication> getLeaveApplicationsByFormNumber(String formNumber);

    Optional<LeaveApplication> getLeaveApplicationById(Long leaveId);

    void deleteLeaveApplication(Long leaveId);

    List<LeaveApplication> getPendingLeaveApplications();

    boolean hasOverlappingApprovedLeaves(String formNumber, java.time.LocalDate startDate, java.time.LocalDate endDate);
}
