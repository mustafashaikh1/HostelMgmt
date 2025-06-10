package com.Hostel.Repository;

import com.Hostel.Entity.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {

    // Find all leave applications by form number (student)
    List<LeaveApplication> findByFormNumberOrderByApplicationDateDesc(String formNumber);

    // Find all leave applications by status
    List<LeaveApplication> findByStatusOrderByApplicationDateDesc(LeaveApplication.LeaveStatus status);

    // Find all pending leave applications
    List<LeaveApplication> findByStatusOrderByApplicationDateAsc(LeaveApplication.LeaveStatus status);

    // Find leave application by ID
    Optional<LeaveApplication> findById(Long leaveId);

    // Check if student has any pending leave applications
    @Query("SELECT COUNT(l) FROM LeaveApplication l WHERE l.formNumber = :formNumber AND l.status = 'PENDING'")
    long countPendingLeavesByFormNumber(@Param("formNumber") String formNumber);

    // Find overlapping leave applications for a student
    @Query("SELECT l FROM LeaveApplication l WHERE l.formNumber = :formNumber " +
            "AND l.status = 'APPROVED' " +
            "AND ((l.startDate <= :endDate AND l.endDate >= :startDate))")
    List<LeaveApplication> findOverlappingApprovedLeaves(@Param("formNumber") String formNumber,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);

    // Get all leave applications with student details
    @Query("SELECT l FROM LeaveApplication l LEFT JOIN FETCH l.hostelForm h LEFT JOIN FETCH h.personalInfo ORDER BY l.applicationDate DESC")
    List<LeaveApplication> findAllWithStudentDetails();

    // Count leaves by date range
    @Query("SELECT COUNT(l) FROM LeaveApplication l WHERE l.applicationDate BETWEEN :startDate AND :endDate")
    long countLeaveApplicationsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}