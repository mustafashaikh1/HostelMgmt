package com.Hostel.Repository;

import com.Hostel.Entity.PersonalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Long> {
    boolean existsByEmail(String email);


}
