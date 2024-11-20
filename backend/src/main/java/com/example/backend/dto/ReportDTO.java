package com.example.backend.dto;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Transactional
public class ReportDTO {
    private Long reporterId; // 신고한 사용자 ID
    private Long reportedId; // 신고당한 사용자 ID
    private Long reviewId; // 신고된 리뷰 ID
}
