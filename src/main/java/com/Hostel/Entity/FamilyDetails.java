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
@Table(name = "family_details")
public class FamilyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long familyDetailsId;  // Renamed field

    @Column(nullable = false)
    private String fatherFullName;

    @Column(nullable = false)
    private String motherSchoolName;

    @Column(nullable = false)
    private String permanentAddress;

    private String city;
    private String state;
    private String pincode;

    @Column(nullable = false, length = 10)
    private String fatherMobileNo;

    @Column(nullable = false, length = 10)
    private String motherMobileNo;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_form_id")
    private HostelForm hostelForm;

}
