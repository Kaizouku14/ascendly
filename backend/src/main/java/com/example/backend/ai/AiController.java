package com.example.backend.ai;


import com.example.backend.ai.dto.*;
import com.example.backend.security.CurrentUserProvider;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ai")
public class AiController {

   private final AiService aiService;
   private final CurrentUserProvider currentUserProvider;

    public AiController(AiService aiService, CurrentUserProvider currentUserProvider) {
        this.aiService = aiService;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping("/chat")
    public ChatResponseDto chat(@Valid @RequestBody ChatRequestDto request) {
        UUID userId = currentUserProvider.getCurrentUserId();
        return aiService.chat(request, userId);
    }

    @GetMapping("/history")
    public List<ChatHistoryDto> getChatHistory() {
        UUID userId = currentUserProvider.getCurrentUserId();
        return aiService.getChatHistory(userId);
    }
}
