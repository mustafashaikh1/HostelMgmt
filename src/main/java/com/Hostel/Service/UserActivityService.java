package com.Hostel.Service;

import com.Hostel.Entity.UserActivity;

import java.util.Optional;

public interface UserActivityService {
    UserActivity addStudyOrWorkDetails(UserActivity userActivity, String formNumber);
    UserActivity updateStudyOrWorkDetails(Long userActivityId, UserActivity updatedActivity);
    Optional<UserActivity> getUserActivityById(Long userActivityId);
    void deleteUserActivity(Long userActivityId);
}
