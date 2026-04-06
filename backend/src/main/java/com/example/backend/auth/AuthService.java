package com.example.backend.auth;

import com.example.backend.auth.application.AuthUseCase;
import com.example.backend.auth.application.port.out.PasswordHasherPort;
import com.example.backend.auth.application.port.out.TokenIssuerPort;
import com.example.backend.auth.application.port.out.UserAccountRepositoryPort;
import com.example.backend.auth.dto.AuthResponse;
import com.example.backend.auth.dto.LoginRequest;
import com.example.backend.auth.dto.RegisterRequest;
import com.example.backend.common.exception.ResourceConflictException;
import com.example.backend.common.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthUseCase {

    private final UserAccountRepositoryPort userAccounts;
    private final PasswordHasherPort passwordHasher;
    private final TokenIssuerPort tokenIssuer;

    AuthService(UserAccountRepositoryPort userAccounts,
                PasswordHasherPort passwordHasher,
                TokenIssuerPort tokenIssuer) {
        this.userAccounts = userAccounts;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userAccounts.existsByEmail(request.getEmail())) {
            throw new ResourceConflictException("Email already in use");
        }

        var account = userAccounts.saveNew(
                request.getUsername(),
                request.getEmail(),
                passwordHasher.hash(request.getPassword()));

        String token = tokenIssuer.issueToken(account.id());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserId(account.id());
        return response;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        var account = userAccounts.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordHasher.matches(request.getPassword(), account.passwordHash())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = tokenIssuer.issueToken(account.id());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserId(account.id());
        return response;
    }

}
