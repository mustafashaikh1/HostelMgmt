package com.Hostel.Service;


import com.Hostel.Entity.HostelRoom;

import java.util.List;

public interface HostelRoomService {
    HostelRoom addRoomToFloor(Long floorId, HostelRoom hostelRoom);
    List<HostelRoom> getRoomsByFloor(Long floorId);
    HostelRoom getRoomById(Long roomId);
    void deleteRoom(Long roomId);
    HostelRoom updateRoom(Long roomId, HostelRoom hostelRoom);
}
