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
@Table(name = "WorkDetails")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class WorkDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workDetailsId;

    private String currentEmployer;
    private String courseEnrolledWorking;
    private String yearOfAdmissionWorking;
    private String workAddress;
    private String city;
    private String state;
    private String pincode;
    private String mobileNo;

    @JsonBackReference // Add this to break recursion on the child side
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_form_id")
    private HostelForm hostelForm;



}
