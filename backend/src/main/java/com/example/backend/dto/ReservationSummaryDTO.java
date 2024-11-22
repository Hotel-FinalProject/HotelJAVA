package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationSummaryDTO {
    private String roomName;      // 객실 이름
    private String userName;      // 예약자 이름
    private String userPhone;     // 예약자 전화번호
    private LocalDate checkIn;    // 체크인 날짜
    private LocalDate checkOut;   // 체크아웃 날짜
    private String request;       // 요청 사항
    private int guest;            // 투숙 인원
    private String status;        // 예약 상태
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Long reservationId;
    private Long roomId;
}
