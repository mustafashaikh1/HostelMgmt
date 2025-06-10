package com.Hostel.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "office_use_only")
public class OfficeUseOnly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long officeUseOnlyId;

    @Column(nullable = false)
    private String floor;

    @Column(nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private String bedType;

    @Column(nullable = false)
    private String roomType;

    @Column(nullable = false)
    private double depositCollected;

    @Column(nullable = false)
    private double totalFees;

    @Column(nullable = false)
    private double remainingFees;

    @Column(length = 500)
    private String remark;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = true)
    private String formNumber;

    @Column(nullable = false)
    private String guardianName;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private LocalDate  admissionDate; // Consider using LocalDate instead of String

    @Column(nullable = false)
    private boolean isActive = true; // Default to true

    // GST-related fields
    @Column(nullable = false)
    private boolean gstIncluded = false; // Default: GST not included

    @Column(nullable = false)
    private double gstPercentage = 0.0; // Default: No GST

    @Column
    private double gstAmount = 0.0; // Default GST amount

    @Column
    private String gstNumber;
    // New fields added for bed allocation
    @Column(name = "bed_number", nullable = false)
    private String bedNumber;


    // Payment-related fields
    @Column
    private String paymentMode;

    @Column
    private String transactionNumber;

    @Column(nullable = false)
    private String conductedBy; // Who conducted the transaction

    // Change the status field to a String type
    @Column(nullable = false)
    private String status = "PENDING"; // Default to "PENDING"



    // New relationship: One OfficeUseOnly can have many payments
    @OneToMany(mappedBy = "officeUseOnly", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Payment> payments = new ArrayList<>();

    public void calculateFees() {
        if (gstIncluded) {
            this.gstAmount = (this.totalFees * this.gstPercentage) / 100;
        } else {
            this.gstAmount = 0.0; // Ensure default GST is 0 if not included
        }

        double totalPaid = payments.stream().mapToDouble(Payment::getAmount).sum();
        this.remainingFees = (this.totalFees + this.gstAmount) - totalPaid - this.depositCollected;
    }



    public void addPayment(Payment payment) {
        payment.setOfficeUseOnly(this);
        this.payments.add(payment);
        calculateFees(); // Update fees after adding a new payment
    }

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_form_id")
    private HostelForm hostelForm;

    @OneToMany(mappedBy = "officeUseOnly", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Payment> payment;


}