package com.Hostel.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hostel_agreement")
public class HostelAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applicantName;
    private String applicantSignatureUrl; // Store URL from S3
    private String parentName;
    private String parentSignatureUrl; // Store URL from S3

    @ManyToOne
    @JoinColumn(name = "hostel_form_id")
    @JsonBackReference // To avoid circular references when serializing to JSON
    private HostelForm hostelForm;
}
