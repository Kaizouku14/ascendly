package com.example.backend.resume;

import com.example.backend.resume.dto.ResumeDto;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ResumeService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String groqApiKey;
    private final String groqModelParser;

    public ResumeService(
        @Value("${groq.api.url}") String groqUrl,
        @Value("${groq.api.key}") String groqApiKey,
        @Value("${groq.model.parser}") String groqModelParser
    ) {
        webClient = WebClient.builder().baseUrl(groqUrl).build();
        this.groqApiKey = groqApiKey;
        this.groqModelParser = groqModelParser;
    }

    public String extractTextFromPdf(MultipartFile file) {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract text from PDF", e);
        }
    }

    private static Map<String, Object> getStringObjectMap(String resumeText, String groqModelParser) {
        String prompt = """
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
                \"""
                %s
                \"""
                """.formatted(resumeText);

        return Map.of(
                "model", groqModelParser,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.1
        );

    }

    @SuppressWarnings({"rawtypes"})
    public ResumeDto parseWithGroq(String resumeText) {
        Map<String, Object> requestBody = getStringObjectMap(resumeText, groqModelParser);

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


}
