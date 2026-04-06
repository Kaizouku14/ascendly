package com.example.backend.ai.infrastructure;

import com.example.backend.ai.application.port.out.AiChatClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class GroqClientAdapter implements AiChatClientPort {

    private final WebClient webClient;
    private final String groqApiKey;

    public GroqClientAdapter(@Value("${groq.api.url}") String groqUrl, @Value("${groq.api.key}") String groqApiKey) {
        this.webClient = WebClient.builder().baseUrl(groqUrl).build();
        this.groqApiKey = groqApiKey;
    }

    @Override
    public String chat(String model, String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.1
        );

        return execute(requestBody);
    }

    @Override
    public String chatWithMessages(String model, List<Map<String, String>> messages) {
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", messages,
                "temperature", 0.7
        );

        return execute(requestBody);
    }

    @SuppressWarnings("rawtypes")
    private String execute(Map<String, Object> requestBody) {
        Map response = webClient.post()
                .header("Authorization", "Bearer " + groqApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List choices = (List) Objects.requireNonNull(response).get("choices");
        Map firstChoice = (Map) choices.getFirst();
        Map message = (Map) firstChoice.get("message");
        return (String) message.get("content");
    }
}
