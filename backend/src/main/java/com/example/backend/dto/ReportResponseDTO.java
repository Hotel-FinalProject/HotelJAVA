package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/* 리뷰 신고의 결과 값 */
public class ReportResponseDTO {
    private Long reportId; // 신고 색인번호
    private String status; // 신고 상태
}
