package com.example.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReportInfoDTO {
    Long reportId;
    String content;
    List<String> imageUrl;
    String reporterName; // 신고자
    String reportedName; // 신고당한사람
    String status; // 신고 상태

}
