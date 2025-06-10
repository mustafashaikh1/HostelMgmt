package com.Hostel.Repository;



import com.Hostel.Entity.HostelInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HostelInquiryRepository extends JpaRepository<HostelInquiry, Long> {


    List<HostelInquiry> findByStudentName(String studentName);
    List<HostelInquiry> findByCity(String city);
    List<HostelInquiry> findByCurrentStatus(String currentStatus); // ✅ Correct Method
    List<HostelInquiry> findByEmail(String email);

    // Count inquiries based on date
    @Query("SELECT COUNT(h) FROM HostelInquiry h WHERE h.inquiryDate = :date")
    long countInquiriesByDate(LocalDate date);

    @Query("SELECT COUNT(h) FROM HostelInquiry h WHERE h.inquiryDate BETWEEN :startDate AND :endDate")
    long countInquiriesBetweenDates(LocalDate startDate, LocalDate endDate);



    @Query("SELECT COUNT(h) FROM HostelInquiry h WHERE h.inquiryDate = :date")
    long countByExactDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(h) FROM HostelInquiry h WHERE h.inquiryDate BETWEEN :startDate AND :endDate")
    long countByDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(o.totalFees) FROM HostelForm h JOIN h.officeUseOnly o WHERE h.date = :date")
    Double getRevenueByExactDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(o.totalFees) FROM HostelForm h JOIN h.officeUseOnly o WHERE h.date BETWEEN :startDate AND :endDate")
    Double getRevenueByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(o.totalFees) FROM HostelForm h JOIN h.officeUseOnly o")
    Double getTotalRevenue();


    @Query("SELECT MONTH(h.inquiryDate), COUNT(h) " +
            "FROM HostelInquiry h " +
            "WHERE YEAR(h.inquiryDate) = :year " +
            "GROUP BY MONTH(h.inquiryDate) " +
            "ORDER BY MONTH(h.inquiryDate)")
    List<Object[]> getMonthlyInquiries(@Param("year") int year);

    @Query("SELECT h.source, COUNT(h) FROM HostelInquiry h WHERE h.inquiryDate BETWEEN :startDate AND :endDate GROUP BY h.source")
    List<Object[]> countInquiriesBySourceBetweenDates(LocalDate startDate, LocalDate endDate);



    @Query("SELECT h.inquiryDate, COUNT(h) " +
            "FROM HostelInquiry h " +
            "WHERE h.monthName = :monthName AND h.year = :year " +
            "GROUP BY h.inquiryDate")
    List<Object[]> getInquiryCountByMonthAndYear(@Param("monthName") String monthName,
                                                 @Param("year") String year);


}
