package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.StudyDetails;
import com.Hostel.Entity.UserActivity;
import com.Hostel.Entity.WorkDetails;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Repository.StudyDetailsRepository;
import com.Hostel.Repository.UserActivityRepository;
import com.Hostel.Repository.WorkDetailsRepository;
import com.Hostel.Service.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserActivityServiceImpl implements UserActivityService {

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private HostelFormRepository hostelFormRepository;

    @Autowired
    private StudyDetailsRepository studyDetailsRepository;

    @Autowired
    private WorkDetailsRepository workDetailsRepository;

    @Override
    public UserActivity addStudyOrWorkDetails(UserActivity userActivity, String formNumber) {
        HostelForm hostelForm = hostelFormRepository.findByFormNumber(formNumber)
                .orElseThrow(() -> new RuntimeException("HostelForm not found with formNumber: " + formNumber));

        userActivity.setHostelForm(hostelForm);

        if ("study".equalsIgnoreCase(userActivity.getActivityType())) {
            if (userActivity.getStudyDetails() != null) {
                StudyDetails studyDetails = userActivity.getStudyDetails();
                studyDetails.setHostelForm(hostelForm);
                studyDetailsRepository.save(studyDetails);
                userActivity.setStudyDetails(studyDetails);
            }
        } else if ("work".equalsIgnoreCase(userActivity.getActivityType())) {
            if (userActivity.getWorkDetails() != null) {
                WorkDetails workDetails = userActivity.getWorkDetails();
                workDetails.setHostelForm(hostelForm);
                workDetailsRepository.save(workDetails);
                userActivity.setWorkDetails(workDetails);
            }
        } else {
            throw new RuntimeException("Invalid activityType: " + userActivity.getActivityType());
        }

        UserActivity savedUserActivity = userActivityRepository.save(userActivity);
        hostelForm.setUserActivity(savedUserActivity);
        hostelFormRepository.save(hostelForm);

        return savedUserActivity;
    }

    @Override
    public UserActivity updateStudyOrWorkDetails(Long userActivityId, UserActivity updatedActivity) {
        UserActivity existingActivity = userActivityRepository.findById(userActivityId)
                .orElseThrow(() -> new RuntimeException("UserActivity not found with id: " + userActivityId));

        if ("study".equalsIgnoreCase(existingActivity.getActivityType()) && updatedActivity.getStudyDetails() != null) {
            StudyDetails studyDetails = existingActivity.getStudyDetails();
            if (studyDetails == null) {
                studyDetails = new StudyDetails();
            }
            if (updatedActivity.getStudyDetails().getCurrentInstitution() != null) {
                studyDetails.setCurrentInstitution(updatedActivity.getStudyDetails().getCurrentInstitution());
            }
            if (updatedActivity.getStudyDetails().getCourseEnrolled() != null) {
                studyDetails.setCourseEnrolled(updatedActivity.getStudyDetails().getCourseEnrolled());
            }
            if (updatedActivity.getStudyDetails().getYearOfAdmission() != null) {
                studyDetails.setYearOfAdmission(updatedActivity.getStudyDetails().getYearOfAdmission());
            }
            studyDetails.setHostelForm(existingActivity.getHostelForm());
            studyDetailsRepository.save(studyDetails);
            existingActivity.setStudyDetails(studyDetails);
        }

        if ("work".equalsIgnoreCase(existingActivity.getActivityType()) && updatedActivity.getWorkDetails() != null) {
            WorkDetails workDetails = existingActivity.getWorkDetails();
            if (workDetails == null) {
                workDetails = new WorkDetails();
            }
            if (updatedActivity.getWorkDetails().getCurrentEmployer() != null) {
                workDetails.setCurrentEmployer(updatedActivity.getWorkDetails().getCurrentEmployer());
            }
            if (updatedActivity.getWorkDetails().getCourseEnrolledWorking() != null) {
                workDetails.setCourseEnrolledWorking(updatedActivity.getWorkDetails().getCourseEnrolledWorking());
            }
            if (updatedActivity.getWorkDetails().getYearOfAdmissionWorking() != null) {
                workDetails.setYearOfAdmissionWorking(updatedActivity.getWorkDetails().getYearOfAdmissionWorking());
            }
            if (updatedActivity.getWorkDetails().getWorkAddress() != null) {
                workDetails.setWorkAddress(updatedActivity.getWorkDetails().getWorkAddress());
            }
            if (updatedActivity.getWorkDetails().getCity() != null) {
                workDetails.setCity(updatedActivity.getWorkDetails().getCity());
            }
            if (updatedActivity.getWorkDetails().getState() != null) {
                workDetails.setState(updatedActivity.getWorkDetails().getState());
            }
            if (updatedActivity.getWorkDetails().getPincode() != null) {
                workDetails.setPincode(updatedActivity.getWorkDetails().getPincode());
            }
            if (updatedActivity.getWorkDetails().getMobileNo() != null) {
                workDetails.setMobileNo(updatedActivity.getWorkDetails().getMobileNo());
            }
            workDetails.setHostelForm(existingActivity.getHostelForm());
            workDetailsRepository.save(workDetails);
            existingActivity.setWorkDetails(workDetails);
        }

        return userActivityRepository.save(existingActivity);
    }

    @Override
    public Optional<UserActivity> getUserActivityById(Long userActivityId) {
        return userActivityRepository.findById(userActivityId);
    }

    @Override
    public void deleteUserActivity(Long userActivityId) {
        Optional<UserActivity> userActivityOpt = userActivityRepository.findById(userActivityId);

        if (userActivityOpt.isPresent()) {
            UserActivity userActivity = userActivityOpt.get();

            // Remove reference from HostelForm to prevent constraint violation
            HostelForm hostelForm = userActivity.getHostelForm();
            if (hostelForm != null) {
                hostelForm.setUserActivity(null);
                hostelFormRepository.save(hostelForm);
            }

            // Now safe to delete UserActivity
            userActivityRepository.deleteById(userActivityId);
        } else {
            throw new RuntimeException("UserActivity not found with ID: " + userActivityId);
        }
    }

}
