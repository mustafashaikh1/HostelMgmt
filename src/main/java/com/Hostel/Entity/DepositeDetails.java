package com.Hostel.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class DepositeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double depositeAmount;
    private Double pendingAmount;
    private Double cancelAmount;
    private Double refundAmount;

    private String depositeStatus;
    private String paymentMode;
    private String transactionNumber;
    private String conductedBy;

    private Boolean gstIncluded;
    private String gstNumber;
    private Double gstPercentage;
    private Double gstAmount;
    private Double totalAmount;

    @CreationTimestamp
    private LocalDate createdDate;

    private String monthName;
    private String year;

    @PrePersist
    @PreUpdate
    public void calculateGstAndTotal() {
        // Initialize new fields if null
        if (pendingAmount == null) pendingAmount = 0.0;
        if (cancelAmount == null) cancelAmount = 0.0;
        if (refundAmount == null) refundAmount = 0.0;

        if (gstIncluded != null && gstIncluded && gstPercentage != null) {
            this.gstAmount = (depositeAmount * gstPercentage) / (100 + gstPercentage);
            this.totalAmount = depositeAmount + gstAmount;
        } else {
            this.gstAmount = 0.0;
            this.totalAmount = depositeAmount;
        }
    }

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_form_id")
    private HostelForm hostelForm;
}