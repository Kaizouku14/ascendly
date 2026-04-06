package com.example.backend.jobs;

import com.example.backend.jobs.dto.JobDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/search")
    public List<JobDto> searchJobs(@RequestParam @NotBlank String query,
                           @RequestParam @NotBlank String location,
                            @RequestParam(required = false) List<String> skills) {
        return jobService.findMatchingJobs(query, location, skills);
    }

}
