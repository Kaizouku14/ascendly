package com.example.backend.auth.application.port.out;

public interface PasswordHasherPort {
    String hash(String rawPassword);

    boolean matches(String rawPassword, String passwordHash);
}
