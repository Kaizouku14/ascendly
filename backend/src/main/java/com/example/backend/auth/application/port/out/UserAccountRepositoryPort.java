package com.example.backend.auth.application.port.out;

import com.example.backend.auth.domain.UserAccount;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepositoryPort {
    boolean existsByEmail(String email);

    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findById(UUID userId);

    UserAccount saveNew(String username, String email, String passwordHash);
}
