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
@Table(name = "LocalGuardianDetails")
public class LocalGuardianDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guardianId;

    @Column(nullable = false)
    private String guardianFullName;

    @Column(nullable = false)
    private String guardianPermanentAddress;

    private String city;

    private String state;

    @Column(nullable = false)
    private String pincode;

    @Column(nullable = false, length = 10)
    private String mobileNo;

    @Column(length = 10)
    private String alternateMobileNo;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_form_id")
    private HostelForm hostelForm;



}
