package com.example.backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String passwd;
    private String role;
}