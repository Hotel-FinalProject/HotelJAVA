package com.example.backend.service;

import com.example.backend.dto.ReportResponseDTO;
import com.example.backend.entity.Report;
import com.example.backend.entity.Reservation;
import com.example.backend.entity.User;
import com.example.backend.repository.ReportRepository;
import com.example.backend.repository.ReservationRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 신고 요청
     * @param reporterId 신고한 사용자
     * @param reportedId 신고당한 사용자
     * @param reservationId 신고된 리뷰 확인
     * @return DB에 해당 정보 저장
     */
    public ReportResponseDTO insertReport(Long reporterId, Long reportedId, Long reservationId) {
        // 신고한 사용자, 신고당한 사용자, 신고된 리뷰 확인
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new IllegalArgumentException("신고한 사용자를 찾을 수 없습니다."));
        User reported = userRepository.findById(reportedId)
                .orElseThrow(() -> new IllegalArgumentException("신고당한 사용자를 찾을 수 없습니다."));
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("신고된 예약을 찾을 수 없습니다."));

        // 신고 생성
        Report report = new Report();
        report.setReporter(reporter);
        report.setReported(reported);
        report.setReservation(reservation);

        // 신고 저장
        Report savedReport = reportRepository.save(report);

        // ReportResponseDTO로 변환하여 반환
        return new ReportResponseDTO(savedReport.getReportId(), savedReport.getStatus());
    }
}
