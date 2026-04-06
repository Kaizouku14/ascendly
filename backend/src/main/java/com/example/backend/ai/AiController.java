package com.example.backend.ai;


import com.example.backend.ai.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiController {

   private final AiService aiService;
   private final ChatHistoryRepository chatHistoryRepository;

    public AiController(AiService aiService, ChatHistoryRepository chatHistoryRepository) {
        this.aiService = aiService;
        this.chatHistoryRepository = chatHistoryRepository;
    }

    @PostMapping("/chat")
    public ChatResponseDto chat(@RequestBody ChatRequestDto request, @RequestParam String userId) {
        return aiService.chat(request, userId);
    }

    @GetMapping("/history")
    public List<ChatHistory> getChatHistory(@RequestParam String userId) {
        return chatHistoryRepository.findByUserIdOrderByCreatedAtAsc(userId);
    }
}
