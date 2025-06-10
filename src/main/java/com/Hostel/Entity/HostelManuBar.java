package com.Hostel.Entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="HostelManuBar")
public class HostelManuBar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hostelManuBarId;

    private String hostelManuBarColor;

    private String hostelManubarImage;
}
