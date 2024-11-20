package com.example.backend.service;

import com.example.backend.dto.ReviewDTO;
import com.example.backend.dto.ReviewResponseDTO;
import com.example.backend.entity.Reservation;
import com.example.backend.entity.Review;
import com.example.backend.entity.User;
import com.example.backend.repository.ReservationRepository;
import com.example.backend.repository.ReviewRepository;
import com.example.backend.repository.UserRepository;
//import com.example.backend.util.S3Handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public ReviewResponseDTO createReview(Long userId, ReviewDTO reviewDTO, List<MultipartFile> images) throws IOException {
        log.info("리뷰 생성 요청 유저 Id : {}", userId);
        log.info("리뷰 생성 요청 DTO : {}", reviewDTO);
        log.info("리뷰 생성 요청 이미지 : {}", images);

        // 예약 정보 확인

        Reservation reservation = reservationRepository.findById(reviewDTO.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다."));

        // 예약의 사용자와 현재 사용자(userId)가 일치하는지 확인
        if (!reservation.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 예약에 대해 리뷰를 작성할 권한이 없습니다.");
        }

        // 유저 정보 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 이미지 업로드 확인 및 처리
        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String fileUrl = s3Service.uploadFile(image); // 업로드 후 URL 반환
                imageUrls.add(fileUrl); // URL을 리스트에 추가
            }
        }

        // 리뷰 생성
        Review review = new Review();
        review.setContent(reviewDTO.getContent());
        review.setRating(reviewDTO.getRating());
        review.setImageUrl(imageUrls); // 이미지 URL 리스트 설정
        review.setUser(user);
        review.setReservation(reservation);
        review.setHotel(reservation.getRooms().getHotel()); // 예약에 해당하는 호텔 정보 설정

        Review savedReview = reviewRepository.save(review);

        // ReviewResponseDTO로 변환하여 반환
        return convertToDTO(savedReview);
    }

    // 리뷰 조회 (단일)
    public ReviewResponseDTO getReview(Long userId, Long reviewId) {
        // 해당 유저의 리뷰 목록을 가져옴
        List<Review> userReviews = reviewRepository.findByUserUserId(userId);

        // 해당 리뷰 목록에서 요청된 리뷰 ID와 일치하는 리뷰를 찾아 반환
        Optional<Review> matchedReview = userReviews.stream()
                .filter(review -> review.getReviewId().equals(reviewId))
                .findFirst();

        // 리뷰가 존재하지 않을 경우 예외 처리
        Review review = matchedReview.orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        return convertToDTO(review);
    }

    // 리뷰 목록 조회 (호텔 별)
    public List<ReviewResponseDTO> getReviewsByHotel(Long hotelId) {
        List<Review> reviews = reviewRepository.findByHotelHotelId(hotelId);
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 유저별 리뷰 조회
    public List<ReviewResponseDTO> getReviewsByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findByUserUserId(userId);
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponseDTO updateReview(Long reviewId, Review updatedReview) {
        Review existingReview = getReviewEntity(reviewId);
        existingReview.setContent(updatedReview.getContent());
        existingReview.setRating(updatedReview.getRating());
        existingReview.setUpdateDate(new Date()); // 수정 날짜 갱신
        return convertToDTO(existingReview);
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // 리뷰 엔터티 가져오기 (내부 메서드)
    private Review getReviewEntity(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));
    }

    // Review 엔터티를 ReviewResponseDTO로 변환하는 메서드
    private ReviewResponseDTO convertToDTO(Review review) {
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO();
        reviewResponseDTO.setReviewId(review.getReviewId());
        reviewResponseDTO.setContent(review.getContent());
        reviewResponseDTO.setRating(review.getRating());
        reviewResponseDTO.setWriteDate(review.getWriteDate());
        reviewResponseDTO.setUpdateDate(review.getUpdateDate());
        reviewResponseDTO.setImageUrl(review.getImageUrl());
        reviewResponseDTO.setReservationId(review.getReservation().getReservationId());
        reviewResponseDTO.setUserId(review.getUser().getUserId());
        reviewResponseDTO.setUserName(review.getUser().getName());
        reviewResponseDTO.setHotelId(review.getHotel().getHotelId());
        reviewResponseDTO.setHotelName(review.getHotel().getName());
        return reviewResponseDTO;
    }
}
