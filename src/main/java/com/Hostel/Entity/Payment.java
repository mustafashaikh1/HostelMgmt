package com.Hostel.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String paymentMode;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false)
    private String transactionNumber; // Unique identifier for the transaction

    @Column(nullable = false)
    private String status; // Payment status (e.g., "Pending", "Completed", "Failed")

    @ManyToOne
    @JoinColumn(name = "office_use_only_id", referencedColumnName = "officeUseOnlyId")
    @JsonBackReference
    private OfficeUseOnly officeUseOnly;



    @Column(name = "form_number", nullable = false)
    private String formNumber;
}
