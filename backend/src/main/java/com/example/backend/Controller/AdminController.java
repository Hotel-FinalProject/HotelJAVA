package com.example.backend.Controller;

import com.example.backend.dto.*;
import com.example.backend.entity.User;
import com.example.backend.service.AdminService;
import com.example.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @PutMapping("/update/{email}")
    public ResponseEntity<String> update(@PathVariable("email") String email) {
        try {
            adminService.update(email);
            return ResponseEntity.ok("암호화 처리 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("암호화 처리 에러: " + e.getMessage());
        }
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return adminService.login(loginRequest.getEmail(), loginRequest.getPasswd());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("이메일 또는 비밀번호가 올바르지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("로그인 처리 중 오류가 발생했습니다.");
        }
    }

    // 호텔 관리자 생성
    //  시스템 관리자인지 확인 후 생성
    @PostMapping("/auth/hotelAdmin-create/{hotelId}")
    public ResponseEntity<?> signUp(
            @RequestHeader("Authorization") String token,
            @RequestBody User user,
            @PathVariable Long hotelId) {
        try {
            // 토큰에서 userId 추출
            String actualToken = token.replace("Bearer ", "");
            Long adminUserId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (adminUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }

            // 권한 확인 및 호텔 관리자 생성
            return adminService.join(user, hotelId, adminUserId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("생성 실패: " + e.getMessage());
        }
    }

    //사용자 조회
    @GetMapping("/auth/user-read")
    public ResponseEntity<?> readUser (@RequestHeader("Authorization") String token){
        try {
            // 토큰에서 userId 추출
            String actualToken = token.replace("Bearer ", "");
            Long adminUserId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (adminUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }
            List<AdminUserDTO> adminUserDTOS = adminService.getUser(adminUserId);
            return ResponseEntity.ok(adminUserDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("조회 실패: " + e.getMessage());
        }
    }

    // 호텔 관리자 조회
    @GetMapping("/auth/hotelAdmin-read")
    public ResponseEntity<?> readHotelAdmin (@RequestHeader("Authorization") String token){
        try {
            // 토큰에서 userId 추출
            String actualToken = token.replace("Bearer ", "");
            Long adminUserId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (adminUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }
            List<AdminUserDTO> adminUserDTOS = adminService.getHotelAdmin(adminUserId);
            return ResponseEntity.ok(adminUserDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("조회 실패: " + e.getMessage());
        }
    }

    // 유저 검색
    @GetMapping("/auth/user-search")
    public ResponseEntity<?> searchUser (@RequestHeader("Authorization") String token,@RequestParam(value = "search", required = false) String search){
        try {
            // 토큰에서 userId 추출
            String actualToken = token.replace("Bearer ", "");
            Long adminUserId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (adminUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }
            List<AdminUserDTO> adminUserDTOS = adminService.getUserSearch(adminUserId,search);
            return ResponseEntity.ok(adminUserDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("조회 실패: " + e.getMessage());
        }
    }


    // 호텔 검색
    @GetMapping("/auth/hotelAdmin-search")
    public ResponseEntity<?> searchHotelAdmin (@RequestHeader("Authorization") String token,@RequestParam(value = "search", required = false) String search){
        try {
            // 토큰에서 userId 추출
            String actualToken = token.replace("Bearer ", "");
            Long adminUserId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (adminUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }
            List<AdminUserDTO> adminUserDTOS = adminService.getHotelAdminSearch(adminUserId,search);
            return ResponseEntity.ok(adminUserDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("조회 실패: " + e.getMessage());
        }
    }

    // 계정상태 변경(유저/호텔 - 활성화/비활성화)
    @PostMapping("/auth/hotel")
    public ResponseEntity<?> searchHotelAdmin (@RequestHeader("Authorization") String token,@RequestParam Long userId){
        try {
            // 토큰에서 userId 추출
            String actualToken = token.replace("Bearer ", "");
            Long adminUserId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (adminUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }
            adminService.getIsActive(adminUserId,userId);
            return ResponseEntity.ok("상태 변경이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("조회 실패: " + e.getMessage());
        }

    }

    // 리뷰 신고 관리
    @PostMapping("/auth/review-report")
    public ResponseEntity<?> reviewReport (@RequestHeader("Authorization") String token,@RequestParam Long reportId){
        try {
            // 토큰에서 userId 추출
            String actualToken = token.replace("Bearer ", "");
            Long adminUserId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (adminUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }
            adminService.isActiveReview(adminUserId,reportId);
            return ResponseEntity.ok("상태 변경이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("조회 실패: " + e.getMessage());
        }
    }

    //신고된 리뷰 조회
    @GetMapping("/auth/review-read")
    public ResponseEntity<?> getReviewReport (@RequestHeader("Authorization") String token){
        try {
            // 토큰에서 userId 추출
            String actualToken = token.replace("Bearer ", "");
            Long adminUserId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (adminUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }
            adminService.getReviewReport(adminUserId);

            List<ReportInfoDTO> reportInfoDTOS = adminService.getReviewReport(adminUserId);
            return ResponseEntity.ok(reportInfoDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("조회 실패: " + e.getMessage());
        }
    }

    //시스템관리자-권한별 전체 사용자 수 / 비활성화 계정
    @GetMapping("/auth/userInfo")
    public ResponseEntity<?> getUserInfo (@RequestHeader("Authorization") String token){
        try {
            // 토큰에서 userId 추출
            String actualToken = token.replace("Bearer ", "");
            Long adminUserId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (adminUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }
            UserInfoDTO userInfo = adminService.getUserInfo(adminUserId);

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("조회 실패: " + e.getMessage());
        }
    }

    //시스템관리자 -리뷰신고 관리 카운트
    @GetMapping("/auth/reviewReportInfo")
    public ResponseEntity<?> getReportInfo (@RequestHeader("Authorization") String token){
        try {
            // 토큰에서 userId 추출
            String actualToken = token.replace("Bearer ", "");
            Long adminUserId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (adminUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }
            ReviewReportInfo reviewReportInfo = adminService.getReportInfo(adminUserId);

            return ResponseEntity.ok(reviewReportInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("조회 실패: " + e.getMessage());
        }
    }



    }
