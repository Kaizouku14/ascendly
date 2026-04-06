package com.example.backend.ai;

import com.example.backend.ai.dto.ChatRequestDto;
import com.example.backend.ai.dto.ChatResponseDto;
import com.example.backend.jobs.dto.JobDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final GroqClient groqClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${groq.model.parser}")
    private String groqModelParser;

    @Value("${groq.model.chat}")
    private String chatModel;

    public AiService(GroqClient groqClient) {
        this.groqClient = groqClient;
    }

    public List<JobDto> rankJobs(List<JobDto> jobs, List<String> skills) {
        String prompt = """
                You are a job matching assistant. Given a list of jobs and a candidate's skills,
                rank the jobs from best match to worst match based on skill relevance.
                Return ONLY a valid JSON array of the same job objects in ranked order. No explanation, no markdown.

                Candidate skills: %s

                Jobs:
                %s
                """.formatted(skills.toString(), toJson(jobs));

        String response = groqClient.chat(groqModelParser, prompt);

        response = response
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

        try {
            JobDto[] ranked = objectMapper.readValue(response, JobDto[].class);
            return Arrays.asList(ranked);
        } catch (Exception e) {
            return jobs;
        }
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "[]";
        }
    }


    public ChatResponseDto chat(ChatRequestDto request) {
        String systemPrompt = """
            You are Upskill, a friendly and professional AI career companion.
            Your goal is to help the user find jobs, improve their resume, and grow their career.
            
            User's skills: %s
            User's target role: %s
            
            Keep responses concise, helpful, and encouraging.
            """.formatted(
                request.getSkills() != null ? request.getSkills().toString() : "Not specified",
                request.getTargetRole() != null ? request.getTargetRole() : "Not specified"
        );

        // Build full message list: system prompt + history + new message
        List<Map<String, String>> messages = new ArrayList<>();

        // System message
        messages.add(Map.of("role", "system", "content", systemPrompt));

        // Previous conversation history from frontend
        if (request.getHistory() != null) {
            request.getHistory().forEach(msg ->
                    messages.add(Map.of("role", msg.getRole(), "content", msg.getContent()))
            );
        }

        // New user message
        messages.add(Map.of("role", "user", "content", request.getMessage()));

        String reply = groqClient.chatWithMessages(chatModel, messages);

        ChatResponseDto response = new ChatResponseDto();
        response.setReply(reply);
        return response;
    }
}
