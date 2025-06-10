package com.Hostel.Controller;



import com.Hostel.Entity.HostelFooter;
import com.Hostel.Service.HostelFooterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class HostelFooterController {

    @Autowired
    private HostelFooterService hostelFooterService;

    @PostMapping("/createHostelFooter")
    public ResponseEntity<?> createHostelFooter(@RequestBody HostelFooter footer) {
        try {
            HostelFooter createdFooter = hostelFooterService.saveFooter(footer);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFooter);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating hostel footer: " + e.getMessage());
        }
    }

    @PutMapping("/updateHostelFooter/{footerId}")
    public ResponseEntity<?> updateHostelFooter(@PathVariable Long footerId, @RequestBody HostelFooter updatedFooter) {
        try {
            HostelFooter result = hostelFooterService.updateFooter(footerId, updatedFooter);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error updating hostel footer: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteHostelFooter/{footerId}")
    public ResponseEntity<String> deleteHostelFooter(@PathVariable Long footerId) {
        try {
            hostelFooterService.deleteFooter(footerId);
            return ResponseEntity.ok("Footer deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error deleting hostel footer: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/getHostelFooterById/{footerId}")
    public ResponseEntity<?> getHostelFooterById(@PathVariable Long footerId) {
        try {
            Optional<HostelFooter> footer = hostelFooterService.getFooterById(footerId);
            return footer.map(ResponseEntity::ok)
                    .orElseThrow(() -> new RuntimeException("Hostel footer not found with ID: " + footerId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error fetching hostel footer: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred: " + e.getMessage());
        }
    }

    // ✅ NEW API: Get All Hostel Footers
    @GetMapping("/getAllHostelFooters")
    public ResponseEntity<?> getAllHostelFooters() {
        try {
            List<HostelFooter> footers = hostelFooterService.getAllFooters();
            if (footers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No hostel footers found.");
            }
            return ResponseEntity.ok(footers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching hostel footers: " + e.getMessage());
        }
    }
}

