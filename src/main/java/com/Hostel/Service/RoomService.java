package com.Hostel.Service;

import com.Hostel.Entity.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RoomService {
    Room addRoom(Long floorId, String roomType, String description, List<MultipartFile> images) throws IOException;
    List<Room> getAllRooms();
    Room getRoomById(Long roomTypeId);
    void deleteRoom(Long roomTypeId);
    Room updateRoomImage(Long roomTypeId, Long imageId, MultipartFile newImage) throws IOException;
    Room deleteRoomImage(Long roomTypeId, Long imageId);
}
