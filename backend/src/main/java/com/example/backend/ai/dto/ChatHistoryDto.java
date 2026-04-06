package com.example.backend.ai.dto;

import java.time.LocalDateTime;

public record ChatHistoryDto(
        String role,
        String content,
        LocalDateTime createdAt
) {
}
