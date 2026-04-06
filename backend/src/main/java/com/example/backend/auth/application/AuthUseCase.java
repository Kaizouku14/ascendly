package com.example.backend.auth.application;

import com.example.backend.auth.dto.AuthResponse;
import com.example.backend.auth.dto.LoginRequest;
import com.example.backend.auth.dto.RegisterRequest;

public interface AuthUseCase {
    AuthResponse register(RegisterRequest command);

    AuthResponse login(LoginRequest command);
}
