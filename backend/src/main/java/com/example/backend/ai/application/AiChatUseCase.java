package com.example.backend.ai.application;

import com.example.backend.ai.dto.ChatHistoryDto;
import com.example.backend.ai.dto.ChatRequestDto;
import com.example.backend.ai.dto.ChatResponseDto;

import java.util.List;
import java.util.UUID;

public interface AiChatUseCase {
    ChatResponseDto chat(ChatRequestDto request, UUID userId);

    List<ChatHistoryDto> getChatHistory(UUID userId);
}
