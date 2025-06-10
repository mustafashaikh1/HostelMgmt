package com.Hostel.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HostelInquiry")
public class HostelInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;

    @Column(nullable = false)
    private String studentName;

    private String fatherName;
    private String fatherMobileNo;
    private String studentMobileNo;
    private String email;
    private String address;
    private String gender;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String taluka;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String state;

    private String pincode;
    private Integer numberOfMembers;
    private String courseOfStudy;

    @Column(nullable = false)
    private LocalDate inquiryDate;

    private String currentStatus; // Pending, Accepted, Rejected

    @Column(nullable = false) // ✅ Removed unique = true
    private String roomType; // Example: "Single", "Double"

    @Column(name = "bed_type", nullable = true)
    private String bedType;

    // ✅ Newly added fields
    private String receptionName;

    private String source;

    private String conductedBy;


    @Column(nullable = true)
    private String monthName;

    private String year;

}
