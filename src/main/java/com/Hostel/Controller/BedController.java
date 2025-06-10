package com.Hostel.Controller;

import com.Hostel.Entity.Bed;
import com.Hostel.Service.BedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/beds")
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
@Slf4j
public class BedController {

    private final BedService bedService;

    public BedController(BedService bedService) {
        this.bedService = bedService;
    }

    // ✅ Add a Bed to a Room
    @PostMapping("/addBedToRoom/{roomId}")
    public ResponseEntity<?> addBedToRoom(@PathVariable Long roomId, @RequestBody Bed bed) {
        try {
            Bed addedBed = bedService.addBedToRoom(roomId, bed);
            return ResponseEntity.ok(addedBed);
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate bed entry for bed number {}: {}", bed.getBedNumber(), e.getMessage());
            return ResponseEntity.badRequest().body("Error: Bed number already exists. Please use a different bed number.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error adding bed to room {}: {}", roomId, e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    // ✅ Get All Beds by Room ID
    @GetMapping("/getBedsByRoom/{roomId}")
    public ResponseEntity<?> getBedsByRoom(@PathVariable Long roomId) {
        try {
            List<Bed> beds = bedService.getBedsByRoom(roomId);
            return ResponseEntity.ok(beds);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error fetching beds for room {}: {}", roomId, e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    // ✅ Get Bed by ID
    @GetMapping("/getBedById/{bedId}")
    public ResponseEntity<?> getBedById(@PathVariable Long bedId) {
        try {
            Bed bed = bedService.getBedById(bedId);
            return ResponseEntity.ok(bed);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error fetching bed {}: {}", bedId, e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    // ✅ Delete Bed
    @DeleteMapping("/deleteBed/{bedId}")
    public ResponseEntity<?> deleteBed(@PathVariable Long bedId) {
        try {
            bedService.deleteBed(bedId);
            return ResponseEntity.ok("Bed deleted successfully!");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting bed {}: {}", bedId, e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    // ✅ Allocate Bed to a Student (HostelForm)
    @PostMapping("/allocate/{bedId}/{hostelFormId}")
    public ResponseEntity<?> allocateBed(@PathVariable Long bedId, @PathVariable Long hostelFormId) {
        try {
            Bed allocatedBed = bedService.allocateBed(bedId, hostelFormId);
            return ResponseEntity.ok(allocatedBed);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error allocating bed {}: {}", bedId, e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    // ✅ Deallocate Bed (Make it available)
    @PostMapping("/deallocate/{bedId}")
    public ResponseEntity<?> deallocateBed(@PathVariable Long bedId) {
        try {
            Bed deallocatedBed = bedService.deallocateBed(bedId);
            return ResponseEntity.ok(deallocatedBed);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error deallocating bed {}: {}", bedId, e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    // ✅ Get Allocated Beds by Form Number
    @GetMapping("/allocated/{formNumber}")
    public ResponseEntity<?> getAllocatedBeds(@PathVariable String formNumber) {
        try {
            List<Bed> allocatedBeds = bedService.getAllocatedBedsByFormNumber(formNumber);
            return ResponseEntity.ok(allocatedBeds);
        } catch (Exception e) {
            log.error("Error fetching allocated beds for form {}: {}", formNumber, e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    // ✅ Get all allocated beds
    @GetMapping("/allocatedBeds")
    public ResponseEntity<?> getAllocatedBeds() {
        try {
            List<Bed> allocatedBeds = bedService.getAllocatedBeds();
            return ResponseEntity.ok(allocatedBeds);
        } catch (Exception e) {
            log.error("Error fetching allocated beds: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    // ✅ Get all unallocated beds
    @GetMapping("/unallocatedBeds")
    public ResponseEntity<?> getUnallocatedBeds() {
        try {
            List<Bed> unallocatedBeds = bedService.getUnallocatedBeds();
            return ResponseEntity.ok(unallocatedBeds);
        } catch (Exception e) {
            log.error("Error fetching unallocated beds: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }
}
