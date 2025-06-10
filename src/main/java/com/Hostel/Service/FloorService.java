package com.Hostel.Service;

import com.Hostel.Dto.FloorResponse;
import com.Hostel.Entity.Floor;

import java.util.List;

public interface FloorService {
    Floor createFloor(String floorName, int totalRooms, String hostelBuildingName, Long adminId);
    List<Floor> getAllFloors();
    Floor getFloorById(Long floorId);
    Floor updateFloor(Long floorId, int totalRooms, String floorName, String hostelBuildingName);
    void deleteFloor(Long floorId);
    int getBedsLeft(Floor floor);
    FloorResponse getFloorDetails(Long floorId);
    int getCapacity(Floor floor);
    List<Floor> getFloorsByBuildingName(String buildingName);


}
