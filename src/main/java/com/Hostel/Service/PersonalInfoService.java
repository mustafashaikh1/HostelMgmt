package com.Hostel.Service;

import com.Hostel.Entity.PersonalInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PersonalInfoService {
    PersonalInfo savePersonalInfo(PersonalInfo personalInfo, MultipartFile personalPhoto, String formNumber);


    PersonalInfo savePersonalInfoByAdmin(Long adminId, PersonalInfo personalInfo, MultipartFile personalPhoto) throws IOException; // ✅ Admin fills form

    List<PersonalInfo> getAllPersonalInfo();
    PersonalInfo getPersonalInfoById(Long personalInfoId);

    PersonalInfo updatePersonalInfoWithImage(Long personalInfoId, PersonalInfo personalInfo, MultipartFile personalPhoto);
    void deletePersonalInfo(Long personalInfoId);

    // ✅ Add this method to allow S3 image upload from the controller

}