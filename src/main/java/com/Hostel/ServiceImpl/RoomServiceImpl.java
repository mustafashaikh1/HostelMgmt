package com.Hostel.ServiceImpl;

import com.Hostel.Entity.Floor;
import com.Hostel.Entity.Room;
import com.Hostel.Repository.FloorRepository;
import com.Hostel.Repository.RoomRepository;
import com.Hostel.Service.RoomService;
import com.Hostel.Service.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;
    private final S3Service s3Service; // AWS S3 Service for handling images

    public RoomServiceImpl(RoomRepository roomRepository, FloorRepository floorRepository, S3Service s3Service) {
        this.roomRepository = roomRepository;
        this.floorRepository = floorRepository;
        this.s3Service = s3Service;
    }

    @Override
    public Room addRoom(Long floorId, String roomType, String description, List<MultipartFile> images) throws IOException {
        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new RuntimeException("Floor not found"));

        Room room = new Room();
        room.setRoomType(roomType);
        room.setDescription(description);

        // Upload images to S3 and store in map
        Map<Long, String> imageMap = new LinkedHashMap<>();
        for (MultipartFile image : images) {
            String imageUrl = s3Service.uploadImage(image);
            imageMap.put(System.currentTimeMillis(), imageUrl);
        }

        room.setImages(imageMap);
        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(Long roomTypeId) {
        return roomRepository.findById(roomTypeId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    @Override
    public void deleteRoom(Long roomTypeId) {
        Room room = roomRepository.findById(roomTypeId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Delete images from S3
        for (String imageUrl : room.getImages().values()) {
            s3Service.deleteImage(imageUrl);
        }

        roomRepository.delete(room);
    }

    @Override
    public Room updateRoomImage(Long roomTypeId, Long imageId, MultipartFile newImage) throws IOException {
        Room room = roomRepository.findById(roomTypeId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!room.getImages().containsKey(imageId)) {
            throw new RuntimeException("Image ID not found");
        }

        // Delete old image from S3
        s3Service.deleteImage(room.getImages().get(imageId));

        // Upload new image to S3
        String newImageUrl = s3Service.uploadImage(newImage);
        room.getImages().put(imageId, newImageUrl);

        return roomRepository.save(room);
    }

    @Override
    public Room deleteRoomImage(Long roomTypeId, Long imageId) {
        Room room = roomRepository.findById(roomTypeId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!room.getImages().containsKey(imageId)) {
            throw new RuntimeException("Image ID not found");
        }

        // Delete image from S3
        s3Service.deleteImage(room.getImages().get(imageId));

        // Remove from map
        room.getImages().remove(imageId);

        return roomRepository.save(room);
    }
}
