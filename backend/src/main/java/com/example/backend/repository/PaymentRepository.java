package com.example.backend.repository;

import com.example.backend.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payments,Long> {
    public long countByTransactionIdContainsIgnoreCase(String imp_uid);
}
