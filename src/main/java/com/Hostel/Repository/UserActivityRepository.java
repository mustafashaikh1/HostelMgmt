package com.Hostel.Repository;



import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    Optional<UserActivity> findByHostelForm(HostelForm hostelForm);

}
