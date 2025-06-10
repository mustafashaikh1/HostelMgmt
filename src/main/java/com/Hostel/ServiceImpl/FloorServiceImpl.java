package com.Hostel.ServiceImpl;

import com.Hostel.Dto.FloorResponse;
import com.Hostel.Entity.Admin;
import com.Hostel.Entity.Bed;
import com.Hostel.Entity.Floor;
import com.Hostel.Entity.HostelRoom;
import com.Hostel.Repository.AdminRepository;
import com.Hostel.Repository.FloorRepository;
import com.Hostel.Service.FloorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class FloorServiceImpl implements FloorService {

    private final FloorRepository floorRepository;
    private final AdminRepository adminRepository;

    public FloorServiceImpl(FloorRepository floorRepository, AdminRepository adminRepository) {
        this.floorRepository = floorRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public Floor createFloor(String floorName, int totalRooms, String hostelBuildingName, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));

        Floor floor = new Floor();
        floor.setHostelBuildingName(hostelBuildingName);
        floor.setFloorName(floorName);
        floor.setTotalRooms(totalRooms);


        return floorRepository.save(floor);
    }

    @Override
    public Floor updateFloor(Long floorId, int totalRooms, String floorName, String hostelBuildingName) {
        Floor floor = floorRepository.findById(floorId).orElseThrow(() -> new RuntimeException("Floor not found"));
        floor.setHostelBuildingName(hostelBuildingName);
        floor.setFloorName(floorName);
        floor.setTotalRooms(totalRooms);


        return floorRepository.save(floor);
    }

    @Override
    public List<Floor> getAllFloors() {
        return floorRepository.findAll();
    }

    @Override
    public Floor getFloorById(Long floorId) {
        return floorRepository.findById(floorId).orElseThrow(() -> new RuntimeException("Floor not found"));
    }

    @Override
    public void deleteFloor(Long floorId) {
        floorRepository.deleteById(floorId);
    }


    @Override
    public int getCapacity(Floor floor) {
        if (floor.getRooms() == null) {
            return 0;  // ✅ If no rooms, capacity is 0
        }
        return floor.getRooms().stream().mapToInt(HostelRoom::getTotalBeds).sum();
    }

    @Override
    public int getBedsLeft(Floor floor) {
        if (floor.getRooms() == null) {
            return 0;  // ✅ No rooms means no beds left
        }
        int allocatedBeds = floor.getRooms().stream().flatMap(room -> room.getBeds() != null ? room.getBeds().stream() : Stream.empty())  // ✅ Handle null beds list
                .filter(Bed::isAllocated).toList().size();
        return getCapacity(floor) - allocatedBeds;
    }


    @Override
    public FloorResponse getFloorDetails(Long floorId) {
        Floor floor = floorRepository.findById(floorId).orElseThrow(() -> new RuntimeException("Floor not found with ID: " + floorId));

        int allocatedBeds = 0;
        int totalBedsPerRoom = 0;  // Will be used for capacity calculation

        if (floor.getRooms() != null && !floor.getRooms().isEmpty()) {
            for (HostelRoom room : floor.getRooms()) {
                totalBedsPerRoom = room.getTotalBeds(); // Assume all rooms have same bed count
                if (room.getBeds() != null) {
                    allocatedBeds += (int) room.getBeds().stream().filter(Bed::isAllocated).count();
                }
            }
        }

        int capacity = floor.getTotalRooms() * totalBedsPerRoom;
        int bedsLeft = capacity - allocatedBeds;

        return new FloorResponse(
                floor.getHostelBuildingName(),
                floor.getFloorName(),
                bedsLeft,
                capacity

        );
    }

    @Override
    public List<Floor> getFloorsByBuildingName(String buildingName) {
        return floorRepository.findByHostelBuildingNameIgnoreCase(buildingName);
    }

}
