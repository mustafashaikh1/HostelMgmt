package com.Hostel.Repository;


import com.Hostel.Entity.RulesAndRegulations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RulesAndRegulationsRepository extends JpaRepository<RulesAndRegulations, Long> {
}
