package com.Hostel.ServiceImpl;

import com.Hostel.Entity.Admin;
import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.PersonalInfo;
import com.Hostel.Repository.AdminRepository;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Repository.PersonalInfoRepository;
import com.Hostel.Service.HostelFormService;
import com.Hostel.Service.PersonalInfoService;
import com.Hostel.Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonalInfoServiceImpl implements PersonalInfoService {

    @Autowired
    private PersonalInfoRepository personalInfoRepository;

    @Autowired
    private HostelFormService hostelFormService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private HostelFormRepository hostelFormRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PersonalInfo savePersonalInfo(PersonalInfo personalInfo, MultipartFile personalPhoto, String formNumber) {
        try {
            // Upload image to S3
            String uploadedImageUrl = s3Service.uploadImage(personalPhoto);
            personalInfo.setPersonalPhoto(uploadedImageUrl);

            // Generate form number if not provided
            if (formNumber == null || formNumber.isEmpty()) {
                formNumber = generateFormNumber();
            }

            // Encode password if provided
            if (personalInfo.getPassword() != null && !personalInfo.getPassword().isEmpty()) {
                personalInfo.setPassword(passwordEncoder.encode(personalInfo.getPassword()));
            }

            HostelForm hostelForm = new HostelForm();
            hostelForm.setFormNumber(formNumber);
            hostelForm = hostelFormService.saveHostelForm(hostelForm);

            personalInfo.setHostelForm(hostelForm);

            return personalInfoRepository.save(personalInfo);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload photo to S3", e);
        }
    }

    @Override
    public PersonalInfo savePersonalInfoByAdmin(Long adminId, PersonalInfo personalInfo, MultipartFile personalPhoto) throws IOException {
        Optional<Admin> adminOpt = adminRepository.findById(adminId);
        if (adminOpt.isEmpty()) {
            throw new RuntimeException("Admin not found");
        }

        String uploadedImageUrl = s3Service.uploadImage(personalPhoto);
        personalInfo.setPersonalPhoto(uploadedImageUrl);
        personalInfo.setAdmin(adminOpt.get());

        // Encode password if provided
        if (personalInfo.getPassword() != null && !personalInfo.getPassword().isEmpty()) {
            personalInfo.setPassword(passwordEncoder.encode(personalInfo.getPassword()));
        }

        HostelForm hostelForm = new HostelForm();
        hostelForm.setFormNumber(generateFormNumber());
        hostelForm = hostelFormService.saveHostelForm(hostelForm);

        personalInfo.setHostelForm(hostelForm);
        return personalInfoRepository.save(personalInfo);
    }

    private String generateFormNumber() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    @Override
    public List<PersonalInfo> getAllPersonalInfo() {
        return personalInfoRepository.findAll();
    }

    @Override
    public PersonalInfo getPersonalInfoById(Long personalInfoId) {
        Optional<PersonalInfo> personalInfo = personalInfoRepository.findById(personalInfoId);
        return personalInfo.orElseThrow(() -> new RuntimeException("Personal Info not found"));
    }

    @Override
    public PersonalInfo updatePersonalInfoWithImage(Long personalInfoId, PersonalInfo personalInfo, MultipartFile personalPhoto) {
        Optional<PersonalInfo> existingPersonalInfoOpt = personalInfoRepository.findById(personalInfoId);

        if (existingPersonalInfoOpt.isEmpty()) {
            throw new RuntimeException("Personal Info not found");
        }

        PersonalInfo existingPersonalInfo = existingPersonalInfoOpt.get();

        // Update text fields
        existingPersonalInfo.setFullName(personalInfo.getFullName());
        existingPersonalInfo.setDateOfBirth(personalInfo.getDateOfBirth());
        existingPersonalInfo.setAge(personalInfo.getAge());
        existingPersonalInfo.setGender(personalInfo.getGender());
        existingPersonalInfo.setMaritalStatus(personalInfo.getMaritalStatus());
        existingPersonalInfo.setBloodGroup(personalInfo.getBloodGroup());
        existingPersonalInfo.setReligion(personalInfo.getReligion());

        // Update password if provided
        if (personalInfo.getPassword() != null && !personalInfo.getPassword().isEmpty()) {
            // Check if confirm password matches
            if (personalInfo.getConfirmPassword() != null &&
                    personalInfo.getPassword().equals(personalInfo.getConfirmPassword())) {
                existingPersonalInfo.setPassword(passwordEncoder.encode(personalInfo.getPassword()));
            } else {
                throw new RuntimeException("Password and confirm password do not match");
            }
        }

        // Update image if a new one is provided
        if (personalPhoto != null && !personalPhoto.isEmpty()) {
            try {
                if (!personalPhoto.getContentType().startsWith("image/")) {
                    throw new RuntimeException("Invalid image format");
                }
                String newPhotoUrl = s3Service.uploadImage(personalPhoto);
                existingPersonalInfo.setPersonalPhoto(newPhotoUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image to S3", e);
            }
        }

        return personalInfoRepository.save(existingPersonalInfo);
    }

    @Override
    public void deletePersonalInfo(Long personalInfoId) {
        personalInfoRepository.deleteById(personalInfoId);
    }
}