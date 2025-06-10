package com.Hostel.Controller;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.Reception;
import com.Hostel.Service.JwtService;
import com.Hostel.Service.ReceptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class ReceptionController {

    @Autowired
    private ReceptionService receptionService;


    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // ✅ Create Reception (Admin assigns Reception)
    // In ReceptionController.java
    @PostMapping("/createReception/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createReception(
            @PathVariable Long adminId,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String mobileNo,
            @RequestParam String secondaryMobileNo,
            @RequestParam String aadharCardNo,
            @RequestParam String address,
            @RequestParam String district,
            @RequestParam String state,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate joiningDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastJobDate,
            @RequestPart(value = "receptionPhoto", required = false) MultipartFile receptionPhoto,
            @RequestPart(value = "idProof", required = false) MultipartFile idProof,
            @RequestPart(value = "addressProof", required = false) MultipartFile addressProof) {

        try {
            Reception reception = new Reception();
            reception.setUsername(username);
            reception.setEmail(email);
            reception.setPassword(password);
            reception.setConfirmPassword(confirmPassword);
            reception.setMobileNo(mobileNo);
            reception.setSecondaryMobileNo(secondaryMobileNo);
            reception.setAadharCardNo(aadharCardNo);
            reception.setAddress(address);
            reception.setDistrict(district);
            reception.setState(state);
            reception.setJoiningDate(joiningDate);
            reception.setLastJobDate(lastJobDate);

            Reception savedReception = receptionService.createReception(reception, adminId, receptionPhoto, idProof, addressProof);
            return ResponseEntity.ok(savedReception);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating Reception: " + e.getMessage());
        }
    }

    @PutMapping("/updateReception/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTION')")
    public ResponseEntity<?> updateReception(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String mobileNo,
            @RequestParam String secondaryMobileNo,
            @RequestParam String aadharCardNo,
            @RequestParam String address,
            @RequestParam String district,
            @RequestParam String state,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate joiningDate,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastJobDate,
            @RequestPart(value = "receptionPhoto", required = false) MultipartFile receptionPhoto,
            @RequestPart(value = "idProof", required = false) MultipartFile idProof,
            @RequestPart(value = "addressProof", required = false) MultipartFile addressProof) {

        try {
            Reception existingReception = receptionService.getReceptionById(id);

            Reception reception = new Reception();
            reception.setUsername(username);
            reception.setEmail(email);
            reception.setMobileNo(mobileNo);
            reception.setSecondaryMobileNo(secondaryMobileNo);
            reception.setAadharCardNo(aadharCardNo);
            reception.setAddress(address);
            reception.setDistrict(district);
            reception.setState(state);
            reception.setJoiningDate(joiningDate);
            reception.setLastJobDate(lastJobDate);

            // Keep existing photo and proofs if no new ones are uploaded
            if (receptionPhoto == null || receptionPhoto.isEmpty()) {
                reception.setReceptionPhoto(existingReception.getReceptionPhoto());
            }
            if (idProof == null || idProof.isEmpty()) {
                reception.setIdProof(existingReception.getIdProof());
            }
            if (addressProof == null || addressProof.isEmpty()) {
                reception.setAddressProof(existingReception.getAddressProof());
            }

            Reception updatedReception = receptionService.updateReception(id, reception, receptionPhoto, idProof, addressProof);
            return ResponseEntity.ok(updatedReception);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reception not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while updating Reception: " + e.getMessage());
        }
    }




    // ✅ Delete Reception
    @DeleteMapping("/deleteReception/{id}")
    public ResponseEntity<String> deleteReception(@PathVariable Long id) {
        try {
            receptionService.deleteReception(id);
            return ResponseEntity.ok("Reception deleted, but image remains in S3.");
        } catch (RuntimeException e) {
            log.error("Reception deletion failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reception deletion failed: " + e.getMessage());
        }
    }

    // ✅ Get Reception by ID
    @GetMapping("/getReceptionById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTION')")
    public ResponseEntity<?> getReceptionById(@PathVariable Long id) {
        try {
            Reception reception = receptionService.getReceptionById(id);
            return ResponseEntity.ok(reception);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reception not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while retrieving Reception: " + e.getMessage());
        }
    }

    // ✅ Get Reception by Email
    @GetMapping("/getReceptionByEmail")
    public ResponseEntity<?> getReceptionByEmail(@RequestParam String email) {
        try {
            Reception reception = receptionService.getReceptionByEmail(email);
            return ResponseEntity.ok(reception);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reception not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while retrieving Reception: " + e.getMessage());
        }
    }

    // ✅ Get All Receptions
    @GetMapping("/getAllReceptions")
    public ResponseEntity<?> getAllReceptions() {
        try {
            List<Reception> receptions = receptionService.getAllReceptions();
            return ResponseEntity.ok(receptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while retrieving Receptions: " + e.getMessage());
        }
    }

    // ✅ Login API for Reception
    @PostMapping("/receptionLogin")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        if (authentication.isAuthenticated()) {
            Reception reception = receptionService.getReceptionByEmail(email);
            if (reception != null) {
                String token = jwtService.generateToken(reception.getEmail(), "RECEPTION");
                Map<String, Object> response = Map.of(
                        "message", "Reception Login Successful!",
                        "receptionId", reception.getId(),
                        "token", token
                );
                return ResponseEntity.ok(response);
            }
        }
        throw new UsernameNotFoundException("Invalid email or password!");
    }

    // ✅ Assign Hostel Forms to Reception
    @PostMapping("/assignHostelForms/{receptionId}")
    public ResponseEntity<?> assignHostelForms(@PathVariable Long receptionId, @RequestBody List<Long> hostelFormIds) {
        try {
            Reception updatedReception = receptionService.assignHostelForms(receptionId, hostelFormIds);
            return ResponseEntity.ok(updatedReception);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reception not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while assigning HostelForms: " + e.getMessage());
        }
    }

    // ✅ Get Hostel Forms Managed by a Reception
    @GetMapping("/getHostelFormsByReceptionId/{receptionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTION')")
    public ResponseEntity<?> getHostelFormsByReceptionId(@PathVariable Long receptionId) {
        try {
            List<HostelForm> hostelForms = receptionService.getHostelFormsByReceptionId(receptionId);
            return ResponseEntity.ok(hostelForms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching HostelForms: " + e.getMessage());
        }
    }

    // ✅ Forgot Password API
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            String message = receptionService.forgotPassword(email);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while processing forgot password: " + e.getMessage());
        }
    }

    // ✅ OTP Verification API
    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam int otp) {
        try {
            String message = receptionService.verifyOtp(email, otp);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while verifying OTP: " + e.getMessage());
        }
    }

    // ✅ Reset Password API
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String newPassword, @RequestParam String confirmPassword) {
        try {
            String message = receptionService.resetPassword(email, newPassword, confirmPassword);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password mismatch: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while resetting password: " + e.getMessage());
        }
    }

    // ✅ Fetch all Reception users assigned to a specific Admin using Admin's Email
    @GetMapping("/admin/{adminEmail}/receptions")
    public ResponseEntity<?> getReceptionsByAdminEmail(@PathVariable String adminEmail) {
        try {
            List<Reception> receptions = receptionService.getReceptionsByAdminEmail(adminEmail);
            return ResponseEntity.ok(receptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching Receptions: " + e.getMessage());
        }
    }
}
