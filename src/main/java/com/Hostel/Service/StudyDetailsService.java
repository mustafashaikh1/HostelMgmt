package com.Hostel.Service;


import com.Hostel.Entity.StudyDetails;

import java.util.Optional;

public interface StudyDetailsService {
    StudyDetails addStudyOrWorkDetails(Object details, String formNumber);
    Optional<StudyDetails> getStudyDetailsById(Long studyDetailsId);
    void deleteStudyDetails(Long studyDetailsId);
}
