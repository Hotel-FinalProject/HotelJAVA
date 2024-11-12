package com.example.backend.dto;

import com.example.backend.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long reservationId; // 예약 ID
    private String method; // 결제 방법
    private String status;
    private BigDecimal amount; // 결제 금액
    private String transactionId; // 거래 ID
    private LocalDateTime date; // 결제 날짜

}
