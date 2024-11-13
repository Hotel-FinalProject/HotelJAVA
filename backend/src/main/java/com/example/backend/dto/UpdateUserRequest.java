package com.example.backend.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String userName;
    private String phone;
}
