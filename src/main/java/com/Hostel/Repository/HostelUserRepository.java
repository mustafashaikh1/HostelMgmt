package com.Hostel.Repository;


import com.Hostel.Entity.HostelUser;
import com.Hostel.Entity.PersonalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostelUserRepository extends JpaRepository<HostelUser, Long> {
    Optional<HostelUser> findByEmail(String email);

    @Query("SELECT p FROM PersonalInfo p WHERE p.email = ?1")
    Optional<PersonalInfo> findPersonalInfoByEmail(String email);
}