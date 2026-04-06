package com.example.backend.jobs.web;

import com.example.backend.jobs.application.JobSearchUseCase;
import com.example.backend.jobs.dto.JobDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobSearchUseCase jobSearchUseCase;

    JobController(JobSearchUseCase jobSearchUseCase) {
        this.jobSearchUseCase = jobSearchUseCase;
    }

    @GetMapping("/search")
    public List<JobDto> searchJobs(@RequestParam @NotBlank String query,
                                   @RequestParam @NotBlank String location,
                                   @RequestParam(required = false) List<String> skills) {
        return jobSearchUseCase.search(query, location, skills);
    }
}
