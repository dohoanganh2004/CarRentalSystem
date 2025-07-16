package com.example.rentalcarsystem.repository;

import com.example.rentalcarsystem.model.PaymentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer> {
    List<PaymentHistory> findPaymentHistoriesByUser_Id(Integer userId);

    Page<PaymentHistory> findByUserIdAndPaymentDateBetween(Integer userId, Instant paymentDateAfter, Instant paymentDateBefore,Pageable pageable);


}
