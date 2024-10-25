package com.example.backend.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class TestController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/greet")
    public String greetAdmin() {
        return "안녕, 관리자!";
    }
}