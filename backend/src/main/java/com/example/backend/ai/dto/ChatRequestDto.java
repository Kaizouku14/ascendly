package com.example.backend.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ChatRequestDto {
    @NotBlank(message = "Message is required")
    @Size(max = 4000, message = "Message must be at most 4000 characters")
    private String message;

    private List<String> skills;

    private String targetRole;

    private List<ChatMessageDto> history;
}
