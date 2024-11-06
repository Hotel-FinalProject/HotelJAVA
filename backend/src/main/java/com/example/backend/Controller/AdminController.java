package com.example.backend.Controller;

import com.example.backend.entity.User;
import com.example.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.POST;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

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

//    @PostMapping("/auth/create")


}
