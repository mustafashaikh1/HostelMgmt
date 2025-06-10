package com.Hostel.Repository;


import com.Hostel.Entity.HostelManuBar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostelManuBarRepository extends JpaRepository<HostelManuBar, Long> {


}
