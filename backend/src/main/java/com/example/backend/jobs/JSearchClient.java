package com.example.backend.jobs;

import com.example.backend.jobs.dto.JobDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JSearchClient {
    private final WebClient webClient;
    private final String apiKey;

    @Value("${jsearch.api.url}")
    private String apiUrl;

    JSearchClient(
            @Value("${jsearch.api.url}") String apiUrl,
            @Value("${jsearch.api.key}") String apiKey
    ) {
        this.webClient = WebClient.create(apiUrl);
        this.apiKey = apiKey;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<JobDto> searchJobs(String query, String location) {
        Map response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("query", query)
                        .queryParam("location", location)
                        .build())
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "jsearch.p.rapidapi.com")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map> data = (List<Map>) Objects.requireNonNull(response).get("data");

        return data.stream().map(job -> {
            JobDto dto = new JobDto();

            dto.setJobId((String) job.get("job_id"));
            dto.setTitle((String) job.get("job_title"));
            dto.setCompany((String) job.get("employer_name"));
            dto.setLocation((String) job.get("job_city"));
            dto.setDescription((String) job.get("job_description"));
            dto.setApplyLink((String) job.get("job_apply_link"));
            dto.setEmploymentType((String) job.get("job_employment_type"));
            dto.setIsRemote((Boolean) job.get("job_is_remote"));

            return dto;
        }).collect(Collectors.toList());
    }
}
