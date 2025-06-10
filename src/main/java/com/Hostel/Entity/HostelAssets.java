package com.Hostel.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostelAssets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assetType;
    private String assetCategory;
    private LocalDate dateOfPurchase;

    private String assetName;
    @Column(unique = true, nullable = false)
    private String modelNumber;


    private BigDecimal purchasePrice;
    private BigDecimal estimatePrice;

    private LocalDate expiryDate; // Can represent validity or warranty

    private String purchasedBy;
    private String managedBy;

    private String assetStatus; // e.g., Active, Inactive, Broken, etc.

    private String quantity;
}
