package com.Hostel.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "beds")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bedId;

    @Column(name = "bed_number", nullable = false, unique = true)
    private String bedNumber;

    @Column(name = "bed_type", nullable = false)
    private String bedType;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @JsonIgnoreProperties("beds")  // Prevents circular reference
    private HostelRoom room;

    @ManyToOne
    @JoinColumn(name = "hostel_form_id")
    @JsonIgnoreProperties("beds")  // Prevents circular reference
    private HostelForm hostelForm;

    @Column(name = "allocated", nullable = false)
    private boolean allocated = false;


    private Boolean available = true;

  
}
