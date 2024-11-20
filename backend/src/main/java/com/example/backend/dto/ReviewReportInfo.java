package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReportInfo {
    long reportCount;//신고된 리뷰 수
    int reportComplete;//신고처리 완료
    int reportInComplete;//신고처리 미완료
}
