package com.Hostel.Repository;


import com.Hostel.Entity.OfficeUseOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfficeUseOnlyRepository extends JpaRepository<OfficeUseOnly, Long> {



    // Sum total revenue collected today
    @Query("SELECT SUM(o.depositCollected) FROM OfficeUseOnly o WHERE o.admissionDate = :date")
    Double sumRevenueByDate(String date);

    // Sum total revenue for a date range
    @Query("SELECT SUM(o.depositCollected) FROM OfficeUseOnly o WHERE o.admissionDate BETWEEN :startDate AND :endDate")
    Double sumRevenueBetweenDates(String startDate, String endDate);








    @Query("SELECT o FROM OfficeUseOnly o LEFT JOIN FETCH o.payment WHERE o.id = :id")
    Optional<OfficeUseOnly> findByIdWithPayment(@Param("id") Long id);

    @Query("SELECT o FROM OfficeUseOnly o LEFT JOIN FETCH o.payment WHERE o.formNumber = :formNumber")
    Optional<OfficeUseOnly> findByFormNumber(@Param("formNumber") String formNumber);
}