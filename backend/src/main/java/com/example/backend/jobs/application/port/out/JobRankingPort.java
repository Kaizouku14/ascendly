package com.example.backend.jobs.application.port.out;

import com.example.backend.jobs.dto.JobDto;

import java.util.List;

public interface JobRankingPort {
    List<JobDto> rankJobs(List<JobDto> jobs, List<String> skills);
}
