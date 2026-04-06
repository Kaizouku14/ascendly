package com.example.backend.resume.infrastructure;

import com.example.backend.resume.application.port.out.ResumeParserPort;
import com.example.backend.resume.dto.ResumeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class GroqResumeParserAdapter implements ResumeParserPort {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String groqApiKey;
    private final String groqModelParser;

    public GroqResumeParserAdapter(
            @Value("${groq.api.url}") String groqUrl,
            @Value("${groq.api.key}") String groqApiKey,
            @Value("${groq.model.parser}") String groqModelParser
    ) {
        this.webClient = WebClient.builder().baseUrl(groqUrl).build();
        this.groqApiKey = groqApiKey;
        this.groqModelParser = groqModelParser;
    }

    @SuppressWarnings({"rawtypes"})
    @Override
    public ResumeDto parse(String resumeText) {
        Map<String, Object> requestBody = Map.of(
                "model", groqModelParser,
                "messages", List.of(Map.of("role", "user", "content", buildPrompt(resumeText))),
                "temperature", 0.1
        );

        Map response = webClient.post()
                .header("Authorization", "Bearer " + groqApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List choices = (List) Objects.requireNonNull(response).get("choices");
        Map firstChoice = (Map) choices.getFirst();
        Map message = (Map) firstChoice.get("message");
        String jsonContent = (String) message.get("content");

        jsonContent = jsonContent
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

        try {
            return objectMapper.readValue(jsonContent, ResumeDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Groq response: " + e.getMessage());
        }
    }

    private String buildPrompt(String resumeText) {
        return """
                You are a resume parser. Extract the following fields from the resume text below and return ONLY a valid JSON object. No explanation, no markdown.

                Fields to extract:
                - fullName (string)
                - email (string)
                - phone (string)
                - location (string)
                - summary (string)
                - targetRole (string — infer from experience if not stated)
                - skills (array of strings)
                - education (array of strings, format: "Degree, School, Year")
                - experience (array of strings, format: "Role at Company — duration")

                Resume:
                \"\"\"
                %s
                \"\"\"
                """.formatted(resumeText);
    }
}
