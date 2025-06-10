package com.Hostel.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "StudyDetails")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StudyDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyDetailsId;  // Renamed the id field to studyDetailsId

    private String currentInstitution;
    private String courseEnrolled;
    private String classStandard;
    private String yearOfAdmission;
    private String institutionAddress;
    private String city;
    private String state;
    private String pincode;
    private String mobileNo;

    @JsonBackReference // Add this to break recursion on the child side
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_form_id")
    private HostelForm hostelForm;


}
