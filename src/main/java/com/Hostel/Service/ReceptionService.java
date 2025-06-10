package com.Hostel.Service;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.Reception;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ReceptionService {

    Reception createReception(Reception reception, Long adminId, MultipartFile receptionPhoto, MultipartFile idProofFile, MultipartFile addressProofFile) throws IOException;

    Reception updateReception(Long id, Reception reception, MultipartFile receptionPhoto, MultipartFile idProofFile, MultipartFile addressProofFile) throws IOException;

    void deleteReception(Long id);

    Reception getReceptionByEmail(String email);

    Reception getReceptionById(Long id);


    // ✅ Fetch all receptions assigned to a specific Admin using Admin's Email
    List<Reception> getReceptionsByAdminEmail(String adminEmail);

    Reception findByEmail(String email);

    List<Reception> getAllReceptions();

    Map<String, Object> login(String email, String password);

    // Method to assign hostel forms to a Reception
    Reception assignHostelForms(Long receptionId, List<Long> hostelFormIds);

    List<HostelForm> getHostelFormsByReceptionId(Long receptionId);

    // Forgot Password, OTP Verification, and Reset Password
    String forgotPassword(String email);
    String verifyOtp(String email, int otp);
    String resetPassword(String email, String newPassword, String confirmPassword);



}
