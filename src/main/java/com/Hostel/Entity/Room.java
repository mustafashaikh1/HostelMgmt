package com.Hostel.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rooms") // ✅ Table name remains "rooms"
public class Room { // ✅ Keeping the name "Room"

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId; // ✅ This is still the primary key

    @Column(nullable = false, unique = true)
    private String roomType; // ✅ Example: "Single", "Double"

    @Column(nullable = false)
    private String description;

    @ElementCollection
    @CollectionTable(name = "room_images", joinColumns = @JoinColumn(name = "room_type_id"))
    @MapKeyColumn(name = "image_id")
    @Column(name = "image_url")
    private Map<Long, String> images = new LinkedHashMap<>(); // ✅ Image ID -> Image URL
}
