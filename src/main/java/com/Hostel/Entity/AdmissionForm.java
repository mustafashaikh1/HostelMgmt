package com.Hostel.Entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "admission_forms")
public class AdmissionForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long admissionId;

    @Column(nullable = false)
    private String formNumber;


    private String studentName;
    private String email;
    private String mobileNo;
    private String roomNumber;
    private String roomType;
    private String bedType;
    private String bedNumber;
    private String floor;
    private String monthName;
    private Double monthRent;
    private String paymentMode;
    @Column(nullable = true)
    private String authorizedSignatureUrl;

    private String year;
    private LocalDate admissionDate;
    private String conductedBy;
    private String source; // Example: "Online", "Referral", "Walk-in"

    private  String paymentStatus;

    @Column(name = "bed_allocation_status")
    private String bedAllocationStatus;


    @Column(name = "transaction_number")
    private String transactionNumber;

    @Column(name = "gst_included")
    private Boolean gstIncluded;

    @Column(name = "gst_percentage")
    private Double gstPercentage;

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(name = "gst_amount")
    private Double gstAmount;

    @Column(name = "total_amount")
    private Double totalAmount;

    private LocalDate fromDate;
    private LocalDate toDate;
    private Double numberOfMonths;
    private Double totalRent;

    @Column(name = "is_active")
    private Boolean isActive;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

}
