package com.Hostel.Service;


import com.Hostel.Dto.AdminDTO;
import com.Hostel.Entity.Admin;

import java.util.List;
import java.util.Map;

public interface AdminService {

    Admin createAdmin(Admin admin);

    Admin getAdminById(Long id);
    Admin getAdminByEmail(String email);
    Admin updateAdmin(Long id, Admin admin);
    void deleteAdmin(Long id);
    Map<String, Object> login(String email, String password);
    List<AdminDTO> getAllAdmins();

    String forgotPassword(String email);
    String verifyOtp(String email, int otp);
    String resetPassword(String email, String newPassword, String confirmPassword);
}
