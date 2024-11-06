package com.example.backend.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.example.backend.dto.PaymentDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment")
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId; // 결제 관리 번호

    private String method; // 결제 방법
    private BigDecimal amount; // 결제 금액

    private Date date; // 결제

    private String transactionId; // 거래 관리 번호 (고유번호)
    private String status;

    @OneToOne(mappedBy = "payment")
    private Reservation reservation;


}

