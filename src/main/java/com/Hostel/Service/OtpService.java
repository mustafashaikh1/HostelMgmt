package com.Hostel.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    private ConcurrentHashMap<String, OTPDetails> otpStorage = new ConcurrentHashMap<>();

    // Generate OTP and send to email
    public boolean generateAndSendOTP(String email) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);  // 6-digit OTP

        OTPDetails otpDetails = new OTPDetails(otp, LocalDateTime.now().plusMinutes(5));
        otpStorage.put(email, otpDetails);

        // Send OTP via email
        sendOtpEmail(email, otp);
        return true;
    }

    // Verify OTP
    public boolean verifyOTP(String email, int otp) {
        OTPDetails otpDetails = otpStorage.get(email);
        if (otpDetails != null && otpDetails.getOtp() == otp && otpDetails.getExpiryTime().isAfter(LocalDateTime.now())) {
            otpStorage.remove(email);
            return true;
        }
        return false;
    }

    // Send OTP email
    private void sendOtpEmail(String email, int otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Your OTP Code");
        message.setTo(email);
        message.setText("Your OTP code is: " + otp + ". Please use this code to reset your password.");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            // Log the error or handle it appropriately
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    // OTP details class
    private static class OTPDetails {
        private int otp;
        private LocalDateTime expiryTime;

        public OTPDetails(int otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public int getOtp() {
            return otp;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }
}

