package com.Hostel.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "our_facilities")
public class OurFacilities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ourFacilitiesId;

    @Column(nullable = false, unique = true)
    private String facilityName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private String  facilityImageUrl;
}

