package com.Hostel.Controller;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.UserActivity;
import com.Hostel.Service.UserActivityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class UserActivityController {

    @Autowired
    private UserActivityService userActivityService;

    @PostMapping("/addStudyOrWorkDetails")
    public ResponseEntity<HostelForm> addStudyOrWorkDetails(
            @RequestParam String formNumber,
            @RequestBody UserActivity userActivity) {
        UserActivity savedUserActivity = userActivityService.addStudyOrWorkDetails(userActivity, formNumber);
        return ResponseEntity.ok(savedUserActivity.getHostelForm());
    }

    @PutMapping("/updateStudyOrWork/{userActivityId}")
    public ResponseEntity<UserActivity> updateStudyOrWorkDetails(
            @PathVariable Long userActivityId,
            @RequestBody UserActivity updatedActivity) {
        UserActivity updatedUserActivity = userActivityService.updateStudyOrWorkDetails(userActivityId, updatedActivity);
        return ResponseEntity.ok(updatedUserActivity);
    }

    @GetMapping("/getUserActivityById/{userActivityId}")
    public ResponseEntity<UserActivity> getUserActivityById(@PathVariable Long userActivityId) {
        Optional<UserActivity> userActivity = userActivityService.getUserActivityById(userActivityId);
        return userActivity.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/deleteUserActivity/{userActivityId}")
    public ResponseEntity<?> deleteUserActivity(@PathVariable Long userActivityId) {
        try {
            userActivityService.deleteUserActivity(userActivityId);
            return ResponseEntity.ok("UserActivity deleted successfully.");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete UserActivity because it is referenced by HostelForm. Remove the association first.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("UserActivity not found with ID: " + userActivityId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting UserActivity: " + e.getMessage());
        }
    }

}
