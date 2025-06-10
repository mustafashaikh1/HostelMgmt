package com.Hostel.Repository;

import com.Hostel.Entity.AdmissionForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionFormRepository extends JpaRepository<AdmissionForm, Long> {
    Optional<AdmissionForm> findByFormNumber(String formNumber);
    @Query("SELECT a.admissionDate, COUNT(a), SUM(a.monthRent) " +
            "FROM AdmissionForm a " +
            "GROUP BY a.admissionDate " +
            "ORDER BY a.admissionDate ASC")
    List<Object[]> getDateWiseAdmissionStats();




    @Query("SELECT a.source, COUNT(a) FROM AdmissionForm a WHERE a.admissionDate BETWEEN :startDate AND :endDate GROUP BY a.source")
    List<Object[]> countAdmissionsBySourceBetweenDates(LocalDate startDate, LocalDate endDate);



    // 🔹 Month-wise grouped by year and month
    @Query("SELECT YEAR(a.admissionDate), MONTH(a.admissionDate), COUNT(a), SUM(a.totalAmount) " +
            "FROM AdmissionForm a " +
            "GROUP BY YEAR(a.admissionDate), MONTH(a.admissionDate) " +
            "ORDER BY YEAR(a.admissionDate), MONTH(a.admissionDate)")
    List<Object[]> getMonthWiseAdmissionStats();


    // 🔹 Year-wise stats
    @Query("SELECT FUNCTION('YEAR', a.admissionDate), COUNT(a), SUM(a.totalAmount) " +
            "FROM AdmissionForm a " +
            "WHERE FUNCTION('YEAR', a.admissionDate) = :year " +
            "GROUP BY FUNCTION('YEAR', a.admissionDate)")
    List<Object[]> getYearWiseAdmissionStatsByYear(@Param("year") int year);




    @Query("SELECT a.admissionDate, a.monthName, COUNT(a), SUM(a.totalAmount) " +
            "FROM AdmissionForm a " +
            "WHERE a.monthName = :monthName AND a.year = :year " +
            "GROUP BY a.admissionDate, a.monthName " +
            "ORDER BY a.admissionDate")
    List<Object[]> getDateWiseSummaryWithMonth(String monthName, String year);





    @Query(value = "SELECT YEAR(admission_date) AS year, MONTH(admission_date) AS month, " +
            "COUNT(*) AS count, SUM(total_amount) AS totalRevenue " +
            "FROM admission_forms " +
            "WHERE YEAR(admission_date) = :year " +
            "GROUP BY YEAR(admission_date), MONTH(admission_date) " +
            "ORDER BY MONTH(admission_date)", nativeQuery = true)
    List<Object[]> getMonthWiseAdmissionStatsByYear(@Param("year") Integer year);

    List<AdmissionForm> findAllByFormNumber(String formNumber);




}