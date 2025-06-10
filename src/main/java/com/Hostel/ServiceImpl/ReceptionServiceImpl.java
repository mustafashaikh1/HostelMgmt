package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.Reception;
import com.Hostel.Repository.AdminRepository;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Repository.ReceptionRepository;
import com.Hostel.Service.OtpService;
import com.Hostel.Service.ReceptionService;
import com.Hostel.Service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class ReceptionServiceImpl implements ReceptionService {

    @Autowired
    private ReceptionRepository receptionRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private HostelFormRepository hostelFormRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // In ReceptionServiceImpl.java
    @Override
    public Reception createReception(Reception reception, Long adminId, MultipartFile file, MultipartFile idProofFile, MultipartFile addressProofFile) throws IOException {
        return adminRepository.findById(adminId).map(admin -> {
            reception.setAdmin(admin);
            reception.setPassword(passwordEncoder.encode(reception.getPassword()));
            reception.setConfirmPassword(passwordEncoder.encode(reception.getConfirmPassword()));

            // Set joining date to current date if not provided
            if (reception.getJoiningDate() == null) {
                reception.setJoiningDate(LocalDate.now());
            }

            // Handle reception photo
            if (file != null && !file.isEmpty()) {
                try {
                    reception.setReceptionPhoto(s3Service.uploadImage(file));
                } catch (IOException e) {
                    throw new RuntimeException("Error uploading reception photo: " + e.getMessage());
                }
            }

            // Handle ID proof file
            if (idProofFile != null && !idProofFile.isEmpty()) {
                try {
                    reception.setIdProof(s3Service.uploadImage(idProofFile));
                } catch (IOException e) {
                    throw new RuntimeException("Error uploading ID proof: " + e.getMessage());
                }
            }

            // Handle Address proof file
            if (addressProofFile != null && !addressProofFile.isEmpty()) {
                try {
                    reception.setAddressProof(s3Service.uploadImage(addressProofFile));
                } catch (IOException e) {
                    throw new RuntimeException("Error uploading address proof: " + e.getMessage());
                }
            }

            return receptionRepository.save(reception);
        }).orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    @Override
    public Reception updateReception(Long id, Reception reception, MultipartFile file, MultipartFile idProofFile, MultipartFile addressProofFile) throws IOException {
        Optional<Reception> existingReceptionOpt = receptionRepository.findById(id);

        if (existingReceptionOpt.isPresent()) {
            Reception existingReception = existingReceptionOpt.get();

            // Update fields
            existingReception.setUsername(reception.getUsername());
            existingReception.setEmail(reception.getEmail());
            existingReception.setMobileNo(reception.getMobileNo());
            existingReception.setSecondaryMobileNo(reception.getSecondaryMobileNo());
            existingReception.setAadharCardNo(reception.getAadharCardNo());
            existingReception.setAddress(reception.getAddress());
            existingReception.setDistrict(reception.getDistrict());
            existingReception.setState(reception.getState());
            existingReception.setJoiningDate(reception.getJoiningDate());
            existingReception.setLastJobDate(reception.getLastJobDate());

            // Handle file uploads if provided
            if (file != null && !file.isEmpty()) {
                String newImageUrl = s3Service.uploadImage(file);
                existingReception.setReceptionPhoto(newImageUrl);
            }

            if (idProofFile != null && !idProofFile.isEmpty()) {
                String newIdProofUrl = s3Service.uploadImage(idProofFile);
                existingReception.setIdProof(newIdProofUrl);
            }

            if (addressProofFile != null && !addressProofFile.isEmpty()) {
                String newAddressProofUrl = s3Service.uploadImage(addressProofFile);
                existingReception.setAddressProof(newAddressProofUrl);
            }

            return receptionRepository.save(existingReception);
        } else {
            throw new RuntimeException("Reception not found with ID: " + id);
        }
    }



    @Override
    public void deleteReception(Long id) {
        Optional<Reception> receptionOpt = receptionRepository.findById(id);

        if (receptionOpt.isEmpty()) {
            throw new RuntimeException("Reception not found with ID: " + id);
        }

        // Fetch the reception object (if needed for logging or other purposes)
        Reception reception = receptionOpt.get();

        // Delete the reception record, but the image remains in S3
        receptionRepository.deleteById(id);

        log.info("Deleted reception with ID: {}, but image remains in S3.", id);
    }

    @Override
    public Reception getReceptionById(Long id) {
        Optional<Reception> reception = receptionRepository.findById(id);
        return reception.orElseThrow(() -> new RuntimeException("Reception not found with id: " + id));
    }

    @Override
    public Reception getReceptionByEmail(String email) {
        return receptionRepository.findByEmail(email);
    }

    @Override
    public List<Reception> getAllReceptions() {
        return receptionRepository.findAll();
    }

    @Override
    public Reception findByEmail(String email) {
        return receptionRepository.findByEmail(email);
    }



    @Override
    public Map<String, Object> login(String email, String password) {
        Reception reception = receptionRepository.findByEmail(email);
        if (reception != null && passwordEncoder.matches(password, reception.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Reception Login Successful!");
            response.put("receptionId", reception.getId());
            return response;
        }
        return Collections.singletonMap("message", "Invalid email or password!");
    }



    // Fetch all Receptions assigned to a specific Admin using Admin's Email
    @Override
    public List<Reception> getReceptionsByAdminEmail(String adminEmail) {
        return receptionRepository.findByAdmin_Email(adminEmail);
    }

    // Assign hostel forms to a Reception
    @Override
    public Reception assignHostelForms(Long receptionId, List<Long> hostelFormIds) {
        Reception reception = receptionRepository.findById(receptionId)
                .orElseThrow(() -> new RuntimeException("Reception not found"));
        List<HostelForm> hostelForms = hostelFormRepository.findAllById(hostelFormIds);
        reception.setHostelForms(hostelForms);
        return receptionRepository.save(reception);
    }

    @Override
    public List<HostelForm> getHostelFormsByReceptionId(Long receptionId) {
        Reception reception = receptionRepository.findById(receptionId)
                .orElseThrow(() -> new RuntimeException("Reception not found"));
        return hostelFormRepository.findByReception(reception);
    }

    @Override
    public String forgotPassword(String email) {
        Optional<Reception> optionalReception = Optional.ofNullable(receptionRepository.findByEmail(email));
        if (optionalReception.isPresent()) {
            boolean otpSent = otpService.generateAndSendOTP(email);
            if (otpSent) {
                return "OTP has been sent to your email: " + email;
            } else {
                throw new IllegalArgumentException("Error in sending OTP. Please try again.");
            }
        } else {
            throw new IllegalArgumentException("Reception email not found!");
        }
    }

    @Override
    public String verifyOtp(String email, int otp) {
        boolean isOtpValid = otpService.verifyOTP(email, otp);
        if (!isOtpValid) {
            throw new IllegalArgumentException("Invalid or expired OTP!");
        }
        return "OTP is valid. You can now reset your password.";
    }

    @Override
    public String resetPassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match!");
        }

        Optional<Reception> optionalReception = Optional.ofNullable(receptionRepository.findByEmail(email));
        if (optionalReception.isPresent()) {
            Reception reception = optionalReception.get();
            reception.setPassword(newPassword);  // You may want to encrypt the password here
            receptionRepository.save(reception);
            return "Password successfully reset.";
        } else {
            throw new IllegalArgumentException("Reception email not found!");
        }
    }
}
