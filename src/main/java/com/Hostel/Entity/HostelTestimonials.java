package com.Hostel.Entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "HostelTestimonials")
public class HostelTestimonials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testimonialId;

    private String testimonialTitle;
    private String testimonialName;
    private String testimonialImage;
    private String description;
    private String testimonialColor;
}
