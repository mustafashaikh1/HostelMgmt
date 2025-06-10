package com.Hostel.ServiceImpl;

import com.Hostel.Entity.Bed;
import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.HostelRoom;
import com.Hostel.Repository.BedRepository;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Repository.HostelRoomRepository;
import com.Hostel.Service.BedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BedServiceImpl implements BedService {

    private final BedRepository bedRepository;
    private final HostelRoomRepository hostelRoomRepository;
    private final HostelFormRepository hostelFormRepository;

    public BedServiceImpl(BedRepository bedRepository, HostelRoomRepository hostelRoomRepository, HostelFormRepository hostelFormRepository) {
        this.bedRepository = bedRepository;
        this.hostelRoomRepository = hostelRoomRepository;
        this.hostelFormRepository = hostelFormRepository;
    }

    @Override
    public Bed addBedToRoom(Long roomId, Bed bed) {
        HostelRoom room = hostelRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        bed.setRoom(room);
        bed.setAllocated(false); // ✅ Ensure it remains false when adding a bed

        return bedRepository.save(bed);
    }


    @Override
    public List<Bed> getBedsByRoom(Long roomId) {
        return bedRepository.findByRoomRoomId(roomId);
    }

    @Override
    public Bed getBedById(Long bedId) {
        return bedRepository.findById(bedId)
                .orElseThrow(() -> new IllegalArgumentException("Bed not found"));
    }

    @Transactional
    @Override
    public void deleteBed(Long bedId) {
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new IllegalArgumentException("Bed not found"));

        if (bed.getHostelForm() != null) {
            throw new IllegalStateException("Cannot delete an allocated bed");
        }

        bedRepository.delete(bed);
    }

    @Transactional
    @Override
    public Bed allocateBed(Long bedId, Long hostelFormId) {
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new IllegalArgumentException("Bed not found"));

        if (bed.getHostelForm() != null) {
            throw new IllegalStateException("Bed is already allocated");
        }

        HostelForm hostelForm = hostelFormRepository.findById(hostelFormId)
                .orElseThrow(() -> new IllegalArgumentException("Hostel Form not found"));

        bed.setHostelForm(hostelForm);
        bed.setAllocated(true); // ✅ Ensure allocated is set to true

        bed = bedRepository.save(bed); // Save the updated bed
        return bed;
    }


    @Transactional
    @Override
    public Bed deallocateBed(Long bedId) {
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new IllegalArgumentException("Bed not found"));
        if (bed.getHostelForm() == null) {
            throw new IllegalStateException("Bed is not allocated to anyone");
        }
        bed.setHostelForm(null);
        bed.setAllocated(false); // ✅ Set allocated to false
        return bedRepository.save(bed);
    }


    @Override
    public List<Bed> getAllocatedBedsByFormNumber(String formNumber) {
        return bedRepository.findByHostelFormFormNumber(formNumber);
    }


    @Override
    public List<Bed> getAllocatedBeds() {
        return bedRepository.findByAllocatedTrue();
    }

    @Override
    public List<Bed> getUnallocatedBeds() {
        return bedRepository.findByAllocatedFalse();
    }

    public int getTotalAllocatedBeds(HostelRoom room) {
        if (room.getBeds() == null) {
            return 0;  // ✅ If beds list is null, return 0 allocated beds
        }
        return (int) room.getBeds().stream().filter(Bed::isAllocated).count();
    }

}
