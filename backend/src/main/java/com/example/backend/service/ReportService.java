package com.example.backend.service;

import com.example.backend.dto.ReportResponseDTO;
import com.example.backend.entity.Report;
import com.example.backend.entity.Review;
import com.example.backend.entity.User;
import com.example.backend.repository.ReportRepository;
import com.example.backend.repository.ReviewRepository;
import com.example.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Lombok의 로깅 기능
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j // 로깅 추가
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public ReportResponseDTO insertReport(Long reporterId, Long reportedId, Long reviewId) {

        // 신고한 사용자 검증 및 조회
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> {
                    log.error("신고한 사용자를 찾을 수 없습니다. ReporterId: {}", reporterId);
                    return new IllegalArgumentException("신고한 사용자를 찾을 수 없습니다.");
                });

        // 신고당한 사용자 검증 및 조회
        User reported = userRepository.findById(reportedId)
                .orElseThrow(() -> {
                    log.error("신고당한 사용자를 찾을 수 없습니다. ReportedId: {}", reportedId);
                    return new IllegalArgumentException("신고당한 사용자를 찾을 수 없습니다.");
                });

        // 신고된 리뷰 검증 및 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    log.error("신고된 리뷰를 찾을 수 없습니다. ReviewId: {}", reviewId);
                    return new IllegalArgumentException("리뷰를 찾을 수 없습니다.");
                });

        // 신고 생성
        Report report = new Report();
        report.setReview(review);
        report.setReporter(reporter);
        report.setReported(reported);

        // 신고 저장
        Report savedReport = reportRepository.save(report);
        log.info("신고 저장 성공 - ReportId: {}", savedReport.getReportId());

        // ReportResponseDTO로 변환하여 반환
        ReportResponseDTO responseDTO = new ReportResponseDTO(savedReport.getReportId(), savedReport.getStatus());
        log.info("신고 응답 생성 성공 - ResponseDTO: {}", responseDTO);

        return responseDTO;
    }
}
