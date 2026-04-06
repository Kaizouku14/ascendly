package com.example.backend.ai.application;

import com.example.backend.jobs.dto.JobDto;

import java.util.List;

public interface JobRankingUseCase {
    List<JobDto> rankJobs(List<JobDto> jobs, List<String> skills);
}
