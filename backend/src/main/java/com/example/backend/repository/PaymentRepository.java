package com.example.backend.repository;

import com.example.backend.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payments,Long> {
    public long countByTransactionIdContainsIgnoreCase(String imp_uid);


    Optional<Payments> findByTransactionId(String imp_uid);
}
