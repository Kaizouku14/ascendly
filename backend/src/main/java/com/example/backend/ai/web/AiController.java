package com.example.backend.ai.web;

import com.example.backend.ai.application.AiChatUseCase;
import com.example.backend.ai.dto.ChatHistoryDto;
import com.example.backend.ai.dto.ChatRequestDto;
import com.example.backend.ai.dto.ChatResponseDto;
import com.example.backend.security.CurrentUserProvider;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ai")
public class AiController {

   private final AiChatUseCase aiChatUseCase;
   private final CurrentUserProvider currentUserProvider;

    public AiController(AiChatUseCase aiChatUseCase, CurrentUserProvider currentUserProvider) {
        this.aiChatUseCase = aiChatUseCase;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping("/chat")
    public ChatResponseDto chat(@Valid @RequestBody ChatRequestDto request) {
        UUID userId = currentUserProvider.getCurrentUserId();
        return aiChatUseCase.chat(request, userId);
    }

    @GetMapping("/history")
    public List<ChatHistoryDto> getChatHistory() {
        UUID userId = currentUserProvider.getCurrentUserId();
        return aiChatUseCase.getChatHistory(userId);
    }
}
