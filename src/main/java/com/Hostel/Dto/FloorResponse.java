package com.Hostel.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FloorResponse {
    private String hostelBuildingName;
    private String floorName;
    private int bedsLeft;
    private int capacity;

}
