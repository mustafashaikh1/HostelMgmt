package com.Hostel.Repository;

import com.Hostel.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.officeUseOnly.formNumber = :formNumber")
    List<Payment> findByFormNumber(@Param("formNumber") String formNumber);
}
