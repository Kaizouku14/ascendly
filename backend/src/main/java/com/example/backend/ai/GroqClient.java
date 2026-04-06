package com.example.backend.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class GroqClient {

   private final WebClient webClient;
   private final String groqApiKey;

    public GroqClient(@Value("${groq.api.url}") String groqUrl, @Value("${groq.api.key}") String groqApiKey) {
        this.webClient = WebClient.builder().baseUrl(groqUrl).build();
        this.groqApiKey = groqApiKey;
    }

    public String chat(String model, String prompt){
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.1
        );

        return getString(requestBody);
    }

    public String chatWithMessages(String model, List<Map<String, String>> messages) {
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", messages,
                "temperature", 0.7
        );

        return getString(requestBody);
    }

    @SuppressWarnings("rawtypes")
    private String getString(Map<String, Object> requestBody) {
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
