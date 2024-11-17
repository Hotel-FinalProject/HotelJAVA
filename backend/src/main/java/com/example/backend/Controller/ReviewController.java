package com.example.backend.Controller;

import com.example.backend.dto.ReviewDTO;
import com.example.backend.dto.ReviewResponseDTO;
import com.example.backend.entity.Review;
import com.example.backend.service.ReviewService;
import com.example.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;

    // 리뷰 작성
    @PostMapping(value = "/api/auth/reviews", consumes = "multipart/form-data")
    public ResponseEntity<?> createReview(@RequestHeader("Authorization") String token, @ModelAttribute ReviewDTO review, @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        try {
            // Bearer 토큰에서 "Bearer " 부분 제거
            String actualToken = token.replace("Bearer ", "");

            // JWT 토큰을 검증하고 userId 추출
            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }

            // 유저 인증이 되었으므로, 서비스에서 리뷰 작성
            ReviewResponseDTO createdReview = reviewService.createReview(userId, review, images);
            return ResponseEntity.ok(createdReview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("오류: " + e.getMessage());
        }
    }

    // 리뷰 조회 (단일)
    @GetMapping("/api/auth/reviews/user-review/{reviewId}")
    public ResponseEntity<?> getReview(@RequestHeader("Authorization") String token, @PathVariable Long reviewId) {
        try {
            String actualToken = token.replace("Bearer ", "");
            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }

            ReviewResponseDTO review = reviewService.getReview(userId, reviewId);
            return ResponseEntity.ok(review);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("오류: " + e.getMessage());
        }
    }

    // 호텔 리뷰 목록
    @GetMapping("/api/reviews/hotel/{hotelId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByHotel(@PathVariable Long hotelId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByHotel(hotelId);
        return ResponseEntity.ok(reviews);
    }

    // 유저별 리뷰 조회
    @GetMapping("/api/auth/reviews/user/{userId}")
    public ResponseEntity<?> getReviewsByUserId(@RequestHeader("Authorization") String token, @PathVariable Long userId) {
        try {
            String actualToken = token.replace("Bearer ", "");
            Long authenticatedUserId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (authenticatedUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }

            List<ReviewResponseDTO> reviews = reviewService.getReviewsByUserId(userId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("오류: " + e.getMessage());
        }
    }

    // 리뷰 수정
    @PutMapping("/api/auth/reviews/{id}")
    public ResponseEntity<?> updateReview(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Review review) {
        try {
            String actualToken = token.replace("Bearer ", "");
            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }

            ReviewResponseDTO updatedReview = reviewService.updateReview(id, review);
            return ResponseEntity.ok(updatedReview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("오류: " + e.getMessage());
        }
    }

    // 리뷰 삭제
    @DeleteMapping("/api/auth/reviews/{id}")
    public ResponseEntity<?> deleteReview(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            String actualToken = token.replace("Bearer ", "");
            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }

            reviewService.deleteReview(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("오류: " + e.getMessage());
        }
    }
}
