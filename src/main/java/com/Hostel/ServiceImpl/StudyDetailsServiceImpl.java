package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.StudyDetails;
import com.Hostel.Entity.UserActivity;
import com.Hostel.Entity.WorkDetails;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Repository.StudyDetailsRepository;
import com.Hostel.Repository.UserActivityRepository;
import com.Hostel.Repository.WorkDetailsRepository;
import com.Hostel.Service.HostelFormService;
import com.Hostel.Service.StudyDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
public class StudyDetailsServiceImpl implements StudyDetailsService {

    @Autowired
    private StudyDetailsRepository studyDetailsRepository;

    @Autowired
    private UserActivityRepository userActivityRepository;


    @Autowired
    private HostelFormService hostelFormService;

    @Autowired
    private HostelFormRepository hostelFormRepository;



    @Autowired
    private WorkDetailsRepository workDetailsRepository;

    @Override
    public StudyDetails addStudyOrWorkDetails(Object details, String formNumber) {
        // Fetch HostelForm by formNumber
        HostelForm hostelForm = hostelFormService.getHostelFormByFormNumber(formNumber);

        if (hostelForm == null) {
            throw new RuntimeException("HostelForm not found with formNumber: " + formNumber);
        }

        // Fetch UserActivity associated with the HostelForm
        UserActivity userActivity = userActivityRepository.findByHostelForm(hostelForm)
                .orElseThrow(() -> new RuntimeException("UserActivity not found for formNumber: " + formNumber));

        // Check if UserActivity is "study" or "work"
        if ("study".equalsIgnoreCase(userActivity.getActivityType()) && details instanceof StudyDetails) {
            StudyDetails studyDetails = (StudyDetails) details;

            // Fetch existing StudyDetails or create a new one
            StudyDetails existingStudyDetails = studyDetailsRepository.findByHostelForm(hostelForm)
                    .orElse(new StudyDetails());

            // Update or set StudyDetails fields
            existingStudyDetails.setHostelForm(hostelForm);
            existingStudyDetails.setCurrentInstitution(studyDetails.getCurrentInstitution());
            existingStudyDetails.setCourseEnrolled(studyDetails.getCourseEnrolled());
            existingStudyDetails.setCity(studyDetails.getCity());
            existingStudyDetails.setState(studyDetails.getState());

            // Save and update the StudyDetails in the repository
            studyDetailsRepository.save(existingStudyDetails);
            userActivity.setStudyDetails(existingStudyDetails);

        } else if ("work".equalsIgnoreCase(userActivity.getActivityType()) && details instanceof WorkDetails) {
            WorkDetails workDetails = (WorkDetails) details;

            // Fetch existing WorkDetails or create a new one
            WorkDetails existingWorkDetails = workDetailsRepository.findByHostelForm(hostelForm)
                    .orElse(new WorkDetails());

            // Update or set WorkDetails fields
            existingWorkDetails.setHostelForm(hostelForm);
            existingWorkDetails.setCurrentEmployer(workDetails.getCurrentEmployer());
            existingWorkDetails.setCity(workDetails.getCity());

            // Save and update the WorkDetails in the repository
            workDetailsRepository.save(existingWorkDetails);
            userActivity.setWorkDetails(existingWorkDetails);
        } else {
            throw new RuntimeException("Invalid activityType or details type provided.");
        }

        // Save updated UserActivity with new details
        return userActivityRepository.save(userActivity).getStudyDetails();
    }



    @Override
    public Optional<StudyDetails> getStudyDetailsById(Long studyDetailsId) {
        return studyDetailsRepository.findById(studyDetailsId);
    }

    @Override
    public void deleteStudyDetails(Long studyDetailsId) {
        studyDetailsRepository.deleteById(studyDetailsId);
    }
}
