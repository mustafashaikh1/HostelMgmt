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
@Table(name = "places_nearby")
public class PlacesNearby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placesNearbyId;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String distance;
    @Column(nullable = false)
    private String travelTime;

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private String placeImageUrl;
}
