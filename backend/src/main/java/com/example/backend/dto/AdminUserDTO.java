package com.example.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserDTO {
    String name;
    String phone;
    String email;
    Boolean isActive;
}
