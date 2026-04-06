package com.example.backend.auth.application.port.out;

import java.util.UUID;

public interface TokenIssuerPort {
    String issueToken(UUID userId);
}
