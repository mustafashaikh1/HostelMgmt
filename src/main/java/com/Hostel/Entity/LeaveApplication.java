package com.Hostel.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "leave_applications")
public class LeaveApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;

    @Column(nullable = false)
    private String formNumber; // Student's form number

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 1000)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column
    private String adminRemarks;

    @Column(nullable = false)
    private LocalDateTime applicationDate = LocalDateTime.now();

    @Column
    private LocalDateTime approvalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_admin_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Admin approvedByAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_form_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private HostelForm hostelForm;

    public enum LeaveStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
