package com.Hostel.Repository;

import com.Hostel.Entity.HostelForm;
import com.Hostel.Entity.Reception;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HostelFormRepository extends JpaRepository<HostelForm, Long> {

    @EntityGraph(attributePaths = {
            "personalInfo",
            "familyDetails",
            "contactDetails",
            "localGuardianDetails",
            "medicalInformation",
            "userActivity",
            "studyDetails",
            "workDetails",
            "vehicleDetails",
            "undertakingForm",
            "admin",
            "beds.room.floor",
            "officeUseOnly",
//            "admissionForm"

    })
    @Query("SELECT DISTINCT h FROM HostelForm h " +
            "LEFT JOIN FETCH h.personalInfo " +
            "LEFT JOIN FETCH h.familyDetails " +
            "LEFT JOIN FETCH h.contactDetails " +
            "LEFT JOIN FETCH h.localGuardianDetails " +
            "LEFT JOIN FETCH h.medicalInformation " +
            "LEFT JOIN FETCH h.userActivity " +
            "LEFT JOIN FETCH h.studyDetails " +
            "LEFT JOIN FETCH h.workDetails " +
            "LEFT JOIN FETCH h.vehicleDetails " +
            "LEFT JOIN FETCH h.undertakingForm " +
            "LEFT JOIN FETCH h.admin " +
            "LEFT JOIN FETCH h.beds b " +
            "LEFT JOIN FETCH b.room r " +
            "LEFT JOIN FETCH r.floor f " +
            "LEFT JOIN FETCH h.officeUseOnly " +
//            "LEFT JOIN FETCH h.admissionForm "+

            "WHERE h.formNumber = :formNumber")
    Optional<HostelForm> findByFormNumber(@Param("formNumber") String formNumber);


    // Fetch all HostelForm records along with related details (Custom Query)
    @EntityGraph(attributePaths = {
            "personalInfo",
            "familyDetails",
            "contactDetails",
            "localGuardianDetails",
            "medicalInformation",
            "userActivity",
            "studyDetails",
            "workDetails",
            "vehicleDetails",
            "undertakingForm",
            "admin",
            "beds.room.floor",
            "officeUseOnly",
//            "admissionForm"
    })
    @Query("SELECT DISTINCT h FROM HostelForm h " +
            "LEFT JOIN FETCH h.personalInfo " +
            "LEFT JOIN FETCH h.familyDetails " +
            "LEFT JOIN FETCH h.contactDetails " +
            "LEFT JOIN FETCH h.localGuardianDetails " +
            "LEFT JOIN FETCH h.medicalInformation " +
            "LEFT JOIN FETCH h.userActivity " +
            "LEFT JOIN FETCH h.studyDetails " +
            "LEFT JOIN FETCH h.workDetails " +
            "LEFT JOIN FETCH h.vehicleDetails " +
            "LEFT JOIN FETCH h.undertakingForm " +
            "LEFT JOIN FETCH h.beds b " +
            "LEFT JOIN FETCH b.room r " +
            "LEFT JOIN FETCH r.floor f " +
            "LEFT JOIN FETCH h.officeUseOnly " +
//            "LEFT JOIN FETCH h.admissionForm "+

            "WHERE h.admin IS NOT NULL OR h.reception IS NOT NULL")
    List<HostelForm> findAllHostelFormsWithDetails();


    // Fetch HostelForm by ID (default from JpaRepository)
    Optional<HostelForm> findById(Long id);

    // Fetch the latest HostelForm (based on highest ID)
    HostelForm findTopByOrderByHostelFormIdDesc();

    // Fetch all HostelForm records associated with a given Reception
    List<HostelForm> findByReception(Reception reception);


    @Transactional
    @Modifying
    @Query("DELETE FROM HostelForm hf WHERE hf.formNumber = :formNumber")
    void deleteByFormNumber(@Param("formNumber") String formNumber);


    long countByDate(LocalDate date);


    boolean existsByFormNumber(String formNumber);


    @Query("SELECT COUNT(h) FROM HostelForm h WHERE h.date = :date")
    long countByExactDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(h) FROM HostelForm h WHERE h.date BETWEEN :startDate AND :endDate")
    long countByDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(o.totalFees) FROM HostelForm h JOIN h.officeUseOnly o WHERE h.date = :date")
    Double getRevenueByExactDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(o.totalFees) FROM HostelForm h JOIN h.officeUseOnly o WHERE h.date BETWEEN :startDate AND :endDate")
    Double getRevenueByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(o.totalFees) FROM HostelForm h JOIN h.officeUseOnly o")
    Double getTotalRevenue();


    @Query("SELECT MONTH(h.date), COUNT(h), COALESCE(SUM(h.officeUseOnly.totalFees), 0.0) " +
            "FROM HostelForm h " +
            "WHERE YEAR(h.date) = :year " +
            "GROUP BY MONTH(h.date) " +
            "ORDER BY MONTH(h.date)")
    List<Object[]> getMonthlyAdmissionsAndRevenue(@Param("year") int year);


}
