package com.example.backend.dto;

import lombok.Data;

@Data
public class ReportDTO {
    private Long reporterId; // 신고한 사용자 ID
    private Long reportedId; // 신고당한 사용자 ID
    private Long reservationId; // 신고된 리뷰의 예약 ID
}
