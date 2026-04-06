package com.example.backend.auth.domain;

import java.util.UUID;

public record UserAccount(
        UUID id,
        String username,
        String email,
        String passwordHash
) {
}
