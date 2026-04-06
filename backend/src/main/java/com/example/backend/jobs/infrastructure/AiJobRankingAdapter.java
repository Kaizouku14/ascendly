package com.example.backend.jobs.infrastructure;

import com.example.backend.ai.application.JobRankingUseCase;
import com.example.backend.jobs.application.port.out.JobRankingPort;
import com.example.backend.jobs.dto.JobDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiJobRankingAdapter implements JobRankingPort {

    private final JobRankingUseCase jobRankingUseCase;

    public AiJobRankingAdapter(JobRankingUseCase jobRankingUseCase) {
        this.jobRankingUseCase = jobRankingUseCase;
    }

    @Override
    public List<JobDto> rankJobs(List<JobDto> jobs, List<String> skills) {
        return jobRankingUseCase.rankJobs(jobs, skills);
    }
}
