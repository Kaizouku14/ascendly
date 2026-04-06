package com.example.backend.auth.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String userId;
}