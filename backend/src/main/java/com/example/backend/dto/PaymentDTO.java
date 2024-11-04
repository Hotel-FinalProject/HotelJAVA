package com.example.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentDTO {
    private Long reservationId; // 예약 ID
    private String method; // 결제 방법
    private BigDecimal amount; // 결제 금액
    private Long transactionId; // 거래 ID
    private Date date; // 결제 날짜
}
