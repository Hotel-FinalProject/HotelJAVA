package com.example.backend.Controller;

import com.example.backend.dto.ReportDTO;
import com.example.backend.dto.ReportResponseDTO;
import com.example.backend.entity.Report;
import com.example.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/reports")
public class ReportController {
    private final ReportService reportService;

    /** 신고 요청 */
    @PostMapping
    public ResponseEntity<ReportResponseDTO> createReport(@RequestBody ReportDTO reportDTO) {
        try {
            // ReportDTO에서 각 ID를 받아 신고를 생성하고 결과 DTO 반환
            ReportResponseDTO response = reportService.insertReport(reportDTO.getReporterId(), reportDTO.getReportedId(), reportDTO.getReviewId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
