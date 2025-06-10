package com.Hostel.ServiceImpl;



import com.Hostel.Entity.Floor;
import com.Hostel.Entity.HostelRoom;
import com.Hostel.Repository.FloorRepository;
import com.Hostel.Repository.HostelRoomRepository;
import com.Hostel.Service.HostelRoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostelRoomServiceImpl implements HostelRoomService {

    private final HostelRoomRepository hostelRoomRepository;
    private final FloorRepository floorRepository;

    public HostelRoomServiceImpl(HostelRoomRepository hostelRoomRepository, FloorRepository floorRepository) {
        this.hostelRoomRepository = hostelRoomRepository;
        this.floorRepository = floorRepository;
    }

    @Override
    public HostelRoom addRoomToFloor(Long floorId, HostelRoom hostelRoom) {
        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new RuntimeException("Floor not found with ID: " + floorId));

        // Associate room with floor
        hostelRoom.setFloor(floor);

        // Ensure totalBeds is set (default to 0 if not provided)
        if (hostelRoom.getTotalBeds() < 0) {
            throw new RuntimeException("Total beds cannot be negative.");
        }

        // Save the room
        HostelRoom savedRoom = hostelRoomRepository.save(hostelRoom);

        // Update floor's room allocation
        if (floor.getTotalRooms() > 0) {
            floor.setTotalRooms(floor.getTotalRooms() - 1);
            floor.setAllocatedRooms(floor.getAllocatedRooms() + 1);
            floorRepository.save(floor);
        } else {
            throw new RuntimeException("No available rooms left to allocate on this floor.");
        }

        return savedRoom;
    }





    @Override
    public HostelRoom updateRoom(Long roomId, HostelRoom hostelRoomDetails) {
        HostelRoom existingRoom = hostelRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + roomId));

        // ✅ Only update the provided fields
        existingRoom.setRoomNumber(hostelRoomDetails.getRoomNumber());
        existingRoom.setRoomType(hostelRoomDetails.getRoomType());
        existingRoom.setTotalBeds(hostelRoomDetails.getTotalBeds());

        // ❌ No need to set floor again — keep existing

        return hostelRoomRepository.save(existingRoom);
    }




    @Override
    public List<HostelRoom> getRoomsByFloor(Long floorId) {
        return hostelRoomRepository.findByFloor_FloorId(floorId);
    }

    @Override
    public HostelRoom getRoomById(Long roomId) {
        return hostelRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + roomId));
    }

//    @Override
//    public void deleteRoom(Long roomId) {
//        HostelRoom room = hostelRoomRepository.findById(roomId)
//                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + roomId));
//
//        Floor floor = room.getFloor();
//
//        // Delete room
//        hostelRoomRepository.delete(room);
//
//        // Decrease totalRooms count
//        floor.setTotalRooms(floor.getTotalRooms() - 1);
//        floorRepository.save(floor);
//    }

    @Override
    public void deleteRoom(Long roomId) {
        HostelRoom room = hostelRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + roomId));

        // Delete only the room
        hostelRoomRepository.delete(room);
    }



}

