package com.example.backend.jobs;

import com.example.backend.jobs.dto.JobDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JSearchClient jSearchClient;

    public JobService(JSearchClient jSearchClient) {
        this.jSearchClient = jSearchClient;
    }

    public List<JobDto> findMatchingJobs(String query, String location, List<String> skills){

       List<JobDto> jobs = jSearchClient.searchJobs(query, location);

       if(skills != null && !skills.isEmpty()){
           jobs = jobs.stream()
                   .filter(job -> job.getDescription() != null
                           && skills.stream().anyMatch(skill -> job.getDescription().toLowerCase().contains(skill.toLowerCase())))
                   .collect(Collectors.toList());
       }

       return jobs;
    }

}
