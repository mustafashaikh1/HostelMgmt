package com.Hostel.Service;

import com.Hostel.Entity.Bed;

import java.util.List;

public interface BedService {
    Bed addBedToRoom(Long roomId, Bed bed);
    List<Bed> getBedsByRoom(Long roomId);
    Bed getBedById(Long bedId);
    void deleteBed(Long bedId);
    Bed allocateBed(Long bedId, Long hostelFormId);
    Bed deallocateBed(Long bedId);
    List<Bed> getAllocatedBedsByFormNumber(String formNumber);
    List<Bed> getAllocatedBeds();
    List<Bed> getUnallocatedBeds();


}
