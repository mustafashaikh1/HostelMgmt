package com.Hostel.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "HostelSlideBar")
public class HostelSlideBar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String slideBarColor;

    // List of image URLs associated with the slide
    @ElementCollection
    @CollectionTable(name = "hostel_slide_images", joinColumns = @JoinColumn(name = "slide_id"))
    @Column(name = "hostel_image_url") // Renamed from 'image_url'
    private List<String> slideImages;

    // List of image URL IDs stored as integers
    @ElementCollection
    @CollectionTable(name = "hostel_image_ids", joinColumns = @JoinColumn(name = "slide_id"))
    @Column(name = "hostel_image_id") // Renamed from 'image_url_id'
    private List<Integer> imageIds;

}
