package com.example.backend.ai.application.port.out;

import java.util.List;
import java.util.Map;

public interface AiChatClientPort {
    String chat(String model, String prompt);

    String chatWithMessages(String model, List<Map<String, String>> messages);
}
