package com.example.backend.ai;

import com.example.backend.ai.dto.ChatHistoryDto;
import com.example.backend.ai.dto.ChatRequestDto;
import com.example.backend.ai.dto.ChatResponseDto;
import com.example.backend.jobs.dto.JobDto;
import com.example.backend.user.User;
import com.example.backend.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AiService {

    private final GroqClient groqClient;
    private final ChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${groq.model.parser}")
    private String groqModelParser;

    @Value("${groq.model.chat}")
    private String chatModel;

    public AiService(GroqClient groqClient, ChatHistoryRepository chatHistoryRepository, UserRepository userRepository) {
        this.groqClient = groqClient;
        this.chatHistoryRepository = chatHistoryRepository;
        this.userRepository = userRepository;
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

    private void saveMessage(User user, String role, String content) {
        ChatHistory chatHistory = new ChatHistory();

        chatHistory.setUser(user);
        chatHistory.setRole(role);
        chatHistory.setContent(content);
        chatHistoryRepository.save(chatHistory);
    }

    public ChatResponseDto chat(ChatRequestDto request, UUID userId) {

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

        messages.add(Map.of("role", "system", "content", systemPrompt));

        // Previous conversation history from frontend
        if (request.getHistory() != null) {
            request.getHistory().forEach(msg ->
                    messages.add(Map.of("role", msg.getRole(), "content", msg.getContent()))
            );
        }

        // User message
        messages.add(Map.of("role", "user", "content", request.getMessage()));

        String reply = groqClient.chatWithMessages(chatModel, messages);

        User user = userRepository.findById(userId).orElseThrow();
        saveMessage(user, "user", request.getMessage());
        saveMessage(user, "assistant", reply);

        ChatResponseDto response = new ChatResponseDto();
        response.setReply(reply);
        return response;
    }

    public List<ChatHistoryDto> getChatHistory(UUID userId) {
        return chatHistoryRepository.findByUserIdOrderByCreatedAtAsc(userId)
                .stream()
                .map(message -> new ChatHistoryDto(
                        message.getRole(),
                        message.getContent(),
                        message.getCreatedAt()))
                .collect(Collectors.toList());
    }

}
