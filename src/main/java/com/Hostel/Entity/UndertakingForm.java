package com.Hostel.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
@Entity
@Table(name = "undertaking_form")
public class UndertakingForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UndertakingFormId;

    private String otherProofType;
    private String otherProofImages;

    private String parentOtherProofType;
    private String parentOtherProofImages;



    @Column(nullable = false)
    private String educationDocumentsImages;
    @Column(nullable = false)
    private String educationDocumentsType; // New field



    private LocalDate stayFrom;
    private LocalDate stayTo;

    @Column(nullable = false)
    private String applicantSignatureImage;

    private String parentSignatureImage;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_form_id")
    private HostelForm hostelForm;



}
