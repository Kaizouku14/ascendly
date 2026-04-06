package com.example.backend.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatRequestDto {
    private String message;
    private List<String> skills;
    private String targetRole;
    private List<ChatMessageDto> history;
}
