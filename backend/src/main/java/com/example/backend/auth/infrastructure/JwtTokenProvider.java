package com.example.backend.auth.infrastructure;

import com.example.backend.auth.JwtUtil;
import com.example.backend.auth.application.port.out.TokenIssuerPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtTokenProvider implements TokenIssuerPort {

    private final JwtUtil jwtUtil;

    public JwtTokenProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String issueToken(UUID userId) {
        return jwtUtil.generateToken(userId);
    }
}
