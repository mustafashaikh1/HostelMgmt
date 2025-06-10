package com.Hostel.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MedicalInformation")
public class MedicalInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicalInfoId;

    private String medicalCondition;

    private String allergies;

    private String medications;

    @Column(nullable = false)
    private String familyDoctorFullName;

    private String city;

    @Column(nullable = false, length = 10)
    private String mobileNo;

    @Column(length = 10)
    private String alternateMobileNo;

    @Column(nullable = false)
    private String emergencyPersonName;

    @Column(nullable = false, length = 10)
    private String emergencyMobileNo;

    private String emergencyCity;

    @Column(nullable = false)
    private String relationWithApplicant;

    @Column(nullable = false, length = 10)
    private String applicantMobileNo;

    @Column(length = 10)
    private String applicantAlternateMobileNo;


    @JsonBackReference // Add this to break recursion on the child side
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_form_id")
    private HostelForm hostelForm;


}
