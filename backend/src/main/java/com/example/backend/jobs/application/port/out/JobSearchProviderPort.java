package com.example.backend.jobs.application.port.out;

import com.example.backend.jobs.dto.JobDto;

import java.util.List;

public interface JobSearchProviderPort {
    List<JobDto> searchJobs(String query, String location);
}
