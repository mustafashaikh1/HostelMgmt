package com.Hostel.Repository;

import com.Hostel.Entity.DepositeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepositeDetailsRepository extends JpaRepository<DepositeDetails, Long> {

    List<DepositeDetails> findByCreatedDateBetween(LocalDate start, LocalDate end);

    Optional<DepositeDetails> findByHostelForm_FormNumber(String formNumber);

    List<DepositeDetails> findByCreatedDate(LocalDate date);


    @Query("SELECT d.createdDate, COUNT(d), SUM(d.totalAmount) " +
            "FROM DepositeDetails d " +
            "WHERE d.monthName = :monthName AND d.year = :year " +
            "GROUP BY d.createdDate")
    List<Object[]> getDepositSummaryByDate(@Param("monthName") String monthName,
                                           @Param("year") String year);
    @Query("SELECT d.monthName, COUNT(d), SUM(d.totalAmount) " +
            "FROM DepositeDetails d " +
            "WHERE d.year = :year " +
            "GROUP BY d.monthName")
    List<Object[]> getMonthWiseDepositSummary(@Param("year") String year);



}
