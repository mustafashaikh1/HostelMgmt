package com.Hostel.ServiceImpl;

import com.Hostel.Dto.AdminDTO;
import com.Hostel.Entity.Admin;
import com.Hostel.Repository.AdminRepository;
import com.Hostel.Service.AdminService;
import com.Hostel.Service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpService otpService;

    @Override
    public Admin createAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setConfirmPassword(passwordEncoder.encode(admin.getConfirmPassword()));
        return adminRepository.save(admin);
    }


    @Override
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Admin updateAdmin(Long id, Admin admin) {
        Admin existingAdmin = adminRepository.findById(id).orElse(null);
        if (existingAdmin != null) {
            existingAdmin.setUsername(admin.getUsername());
            existingAdmin.setMobileNumber(admin.getMobileNumber());
            existingAdmin.setEmail(admin.getEmail());
            existingAdmin.setPassword(admin.getPassword()); // Consider encrypting before saving
            return adminRepository.save(existingAdmin);
        }
        return null;
    }

    @Override
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public Map<String, Object> login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email).orElse(null);

        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login Successful!");
            response.put("adminId", admin.getId());
            return response;
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Invalid email or password!");
        return errorResponse;
    }


    @Override
    public List<AdminDTO> getAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        List<AdminDTO> adminDTOs = new ArrayList<>();

        for (Admin admin : admins) {
            AdminDTO dto = new AdminDTO(
                    admin.getId(),
                    admin.getEmail(),
                    admin.getUsername(),
                    admin.getMobileNumber(),
                    admin.getReceptions()
            );
            adminDTOs.add(dto);
        }

        return adminDTOs;
    }


    @Override
    public String forgotPassword(String email) {
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        if (optionalAdmin.isPresent()) {
            boolean otpSent = otpService.generateAndSendOTP(email);
            if (otpSent) {
                return "OTP has been sent to your email: " + email;
            } else {
                throw new IllegalArgumentException("Error in sending OTP. Please try again.");
            }
        } else {
            throw new IllegalArgumentException("Admin email not found!");
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

        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            admin.setPassword(newPassword); // Consider encrypting the password before saving
            adminRepository.save(admin);
            return "Password successfully reset.";
        } else {
            throw new IllegalArgumentException("Admin email not found!");
        }
    }
}
