package com.example.backend.ai;

import com.example.backend.jobs.dto.JobDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

@Service
public class AIService {

    private final GroqClient groqClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${groq.model.parser}")
    private String groqModelParser;

    public AIService(GroqClient groqClient) {
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
}
