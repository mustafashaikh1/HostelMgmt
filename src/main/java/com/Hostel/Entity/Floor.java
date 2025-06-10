package com.Hostel.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "floors")
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long floorId;


    @Column(name = "hostel_building_name", nullable = false)
    private String hostelBuildingName;

    @Column(name = "floor_name", nullable = false)
    private String floorName;


    @Column(nullable = false)
    private int totalRooms;


    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<HostelRoom> rooms = new ArrayList<>();  // ✅ Initialize to prevent null


    @Column(nullable = false)
    private int allocatedRooms = 0;


    public int getBedsLeft() {
        int allocatedBeds = rooms.stream()
                .flatMap(room -> room.getBeds().stream())
                .filter(Bed::isAllocated)
                .toList()
                .size();
        return getCapacity() - allocatedBeds;
    }

    public int getCapacity() {
        if (rooms == null || rooms.isEmpty()) {
            return 0;
        }
        int bedsPerRoom = rooms.stream()
                .mapToInt(HostelRoom::getTotalBeds)
                .findFirst()  // Assumes all rooms have the same total beds
                .orElse(0);
        return totalRooms * bedsPerRoom; // ✅ totalRooms * totalBeds per room
    }


}
