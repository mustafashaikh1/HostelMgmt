package com.Hostel.Repository;



import com.Hostel.Entity.HostelSlideBar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostelSlideBarRepository extends JpaRepository<HostelSlideBar, Long> {
}
