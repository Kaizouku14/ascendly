package com.example.backend.jobs.application;

import com.example.backend.jobs.dto.JobDto;

import java.util.List;

public interface JobSearchUseCase {
    List<JobDto> search(String query, String location, List<String> skills);
}
