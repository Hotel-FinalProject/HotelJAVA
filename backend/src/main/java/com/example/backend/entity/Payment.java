package com.example.backend.entity;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId; // 결제 관리 번호

    private String method; // 결제 방법
    private BigDecimal amount; // 결제 금액
    private Date date; // 결제 날짜
    private String transactionId; // 거래 관리 번호 (고유번호)

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false) // 예약 관리 번호 (외래키)
    private Reservation reservation;
}

