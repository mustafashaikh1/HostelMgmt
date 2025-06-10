package com.Hostel.Controller;

import com.Hostel.Entity.Admin;
import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.PersonalInfo;
import com.Hostel.Entity.Reception;
import com.Hostel.Repository.AdminRepository;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Repository.PersonalInfoRepository;
import com.Hostel.Repository.ReceptionRepository;
import com.Hostel.Service.EmailsendService;
import com.Hostel.Service.PersonalInfoService;
import com.Hostel.Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class PersonalInfoController {

    @Autowired
    private PersonalInfoService personalInfoService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private HostelFormRepository hostelFormRepository;

    @Autowired
    private PersonalInfoRepository personalInfoRepository;

    @Autowired
    private ReceptionRepository receptionRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailsendService emailSendService;


    @PostMapping("/createPersonalInfo/{receptionId}")
    public ResponseEntity<Map<String, Object>> createPersonalInfo(
            @PathVariable(required = false) Long receptionId,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) LocalDate dateOfBirth,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String maritalStatus,
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String religion,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String confirmPassword,
            @RequestParam("personalPhoto") MultipartFile personalPhoto) throws IOException {

        Reception reception = receptionRepository.findById(receptionId)
                .orElseThrow(() -> new RuntimeException("Reception not found"));

        Long lastFormNumber = hostelFormRepository.findTopByOrderByHostelFormIdDesc() != null ?
                hostelFormRepository.findTopByOrderByHostelFormIdDesc().getHostelFormId() : 0L;
        String formNumber = String.valueOf(lastFormNumber + 1);


        HostelForm hostelForm = new HostelForm();
        hostelForm.setFormNumber(formNumber);
        hostelForm.setDate(LocalDate.now());
        hostelForm.setReception(reception);
        hostelForm = hostelFormRepository.save(hostelForm);

        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setFullName(fullName);
        personalInfo.setDateOfBirth(dateOfBirth);
        personalInfo.setAge(age);
        personalInfo.setGender(gender);
        personalInfo.setMaritalStatus(maritalStatus);
        personalInfo.setBloodGroup(bloodGroup);
        personalInfo.setReligion(religion);
        personalInfo.setEmail(email);

        // Validate password if provided
        if (password != null && !password.isEmpty()) {
            if (confirmPassword != null && password.equals(confirmPassword)) {
                personalInfo.setPassword(passwordEncoder.encode(password));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Password and confirm password do not match"));
            }
        }

        if (personalPhoto != null && !personalPhoto.isEmpty()) {
            if (!personalPhoto.getContentType().startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid image format"));
            }
            String photoUrl = s3Service.uploadImage(personalPhoto);
            personalInfo.setPersonalPhoto(photoUrl);
        }
        if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
            String subject = "Hostel Registration Confirmation";
            String emailBody = String.format(
                    "Hello %s,\n\nWelcome to our Hostel Management System!\n\n" +
                            "Your registration has been completed successfully. Below are your login credentials and registration details:\n\n" +
                            "📄 Form Number: %s\n📧 Email: %s\n🔐 Password: %s\n\n" +
                            "Please keep this information safe and do not share it with anyone.\n\n" +
                            "If you have any questions or need assistance, feel free to reach out to our support team.\n\n" +
                            "Best regards,\nHostel Management Team",
                    fullName, formNumber, email, password
            );


            emailSendService.sendMail(email, subject, emailBody);
        }

        personalInfo.setHostelForm(hostelForm);
        PersonalInfo savedPersonalInfo = personalInfoRepository.save(personalInfo);
        hostelForm.setPersonalInfo(savedPersonalInfo);
        hostelFormRepository.save(hostelForm);

        // Prepare response with additional fields
        Map<String, Object> response = new HashMap<>();
        response.put("receptionId", reception.getId());
        response.put("receptionName", reception.getUsername());
        response.put("hostelForm", hostelForm);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/createPersonalInfoByAdmin/admin/{adminId}")
    public ResponseEntity<?> createPersonalInfoByAdmin(
            @PathVariable(required = false) Long adminId,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) LocalDate dateOfBirth,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String maritalStatus,
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String religion,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String confirmPassword,
            @RequestParam("personalPhoto") MultipartFile personalPhoto) throws IOException {

        // Fetch Admin
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Generate Form Number
        HostelForm lastForm = hostelFormRepository.findTopByOrderByHostelFormIdDesc();

        // Generate Form Number
        Long lastFormNumber = hostelFormRepository.findTopByOrderByHostelFormIdDesc() != null ?
                hostelFormRepository.findTopByOrderByHostelFormIdDesc().getHostelFormId() : 0L;
        String formNumber = String.valueOf(lastFormNumber + 1);


        // Create New Hostel Form
        HostelForm hostelForm = new HostelForm();
        hostelForm.setFormNumber(formNumber);
        hostelForm.setDate(LocalDate.now());
        hostelForm.setAdmin(admin); // ✅ Linking to Admin
        hostelForm = hostelFormRepository.save(hostelForm);

        // Create Personal Info
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setFullName(fullName);
        personalInfo.setDateOfBirth(dateOfBirth);
        personalInfo.setAge(age);
        personalInfo.setGender(gender);
        personalInfo.setMaritalStatus(maritalStatus);
        personalInfo.setBloodGroup(bloodGroup);
        personalInfo.setReligion(religion);
        personalInfo.setEmail(email);

        // Validate password if provided
        if (password != null && !password.isEmpty()) {
            if (confirmPassword != null && password.equals(confirmPassword)) {
                personalInfo.setPassword(passwordEncoder.encode(password));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Password and confirm password do not match"));
            }
        }

        if (email != null && personalInfoRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email already exists. Please use a different email."));
        }

        if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
            String subject = "Hostel Registration Confirmation";
            String emailBody = String.format(
                    "Hello %s,\n\nWelcome to our Hostel Management System!\n\n" +
                            "Your registration has been completed successfully. Below are your login credentials and registration details:\n\n" +
                            "📄 Roll Number: %s\n📧 Email: %s\n🔐 Password: %s\n\n" +
                            "Please keep this information safe and do not share it with anyone.\n\n" +
                            "If you have any questions or need assistance, feel free to reach out to our support team.\n\n" +
                            "Best regards,\nHostel Management Team",
                    fullName, formNumber, email, password
            );


            emailSendService.sendMail(email, subject, emailBody);
        }

        // Upload Photo to AWS S3
        if (personalPhoto != null && !personalPhoto.isEmpty()) {
            if (!personalPhoto.getContentType().startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid image format"));
            }
            String photoUrl = s3Service.uploadImage(personalPhoto);
            personalInfo.setPersonalPhoto(photoUrl);
        }

        personalInfo.setHostelForm(hostelForm);
        personalInfo = personalInfoRepository.save(personalInfo);

        // Link PersonalInfo to HostelForm
        hostelForm.setPersonalInfo(personalInfo);
        hostelFormRepository.save(hostelForm);

        return ResponseEntity.ok(hostelForm);
    }

    @GetMapping("/getAllPersonalInfo")
    public ResponseEntity<List<PersonalInfo>> getAllPersonalInfo() {
        return ResponseEntity.ok(personalInfoService.getAllPersonalInfo());
    }

    @GetMapping("/getPersonalInfoById/{personalInfoId}")
    public ResponseEntity<PersonalInfo> getPersonalInfoById(@PathVariable Long personalInfoId) {
        return ResponseEntity.ok(personalInfoService.getPersonalInfoById(personalInfoId));
    }

    @PutMapping("/updatePersonalInformation/{personalInfoId}")
    public ResponseEntity<?> updatePersonalInfoWithImage(
            @PathVariable Long personalInfoId,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) LocalDate dateOfBirth,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String maritalStatus,
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String religion,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String confirmPassword,
            @RequestParam(value = "personalPhoto", required = false) MultipartFile personalPhoto) {

        try {
            PersonalInfo personalInfo = new PersonalInfo();
            personalInfo.setFullName(fullName);
            personalInfo.setDateOfBirth(dateOfBirth);
            personalInfo.setAge(age);
            personalInfo.setGender(gender);
            personalInfo.setMaritalStatus(maritalStatus);
            personalInfo.setBloodGroup(bloodGroup);
            personalInfo.setReligion(religion);
            personalInfo.setEmail(email);

            // Set password and confirm password for validation
            if (password != null && !password.isEmpty()) {
                if (confirmPassword != null && password.equals(confirmPassword)) {
                    personalInfo.setPassword(password);
                    personalInfo.setConfirmPassword(confirmPassword);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Password and confirm password do not match"));
                }
            }
            if (email != null && personalInfoRepository.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Email already exists. Please use a different email."));
            }

            PersonalInfo updatedPersonalInfo = personalInfoService.updatePersonalInfoWithImage(personalInfoId, personalInfo, personalPhoto);
            return ResponseEntity.ok(updatedPersonalInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/deletePersonalInfo/{personalInfoId}")
    public ResponseEntity<String> deletePersonalInfo(@PathVariable Long personalInfoId) {
        personalInfoService.deletePersonalInfo(personalInfoId);
        return ResponseEntity.ok("Personal Info deleted successfully");
    }
}