package com.example.backend.jobs;

import com.example.backend.ai.AiService;
import com.example.backend.jobs.dto.JobDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JSearchClient jSearchClient;
    private final AiService aiService;

    public JobService(JSearchClient jSearchClient, AiService aiService) {
        this.jSearchClient = jSearchClient;
        this.aiService = aiService;
    }

    public List<JobDto> findMatchingJobs(String query, String location, List<String> skills){

       List<JobDto> jobs = jSearchClient.searchJobs(query, location);
       String normalizedLocation = location == null ? "" : location.toLowerCase().trim();
       String countryCode = resolveCountryCode(location);

         jobs = jobs.stream()
                 .filter(job ->
                         Boolean.TRUE.equals(job.getIsRemote()) || matchesLocation(job, normalizedLocation, countryCode)
                 )
                 .collect(Collectors.toList());

       if(skills != null && !skills.isEmpty()){
           jobs = jobs.stream()
                   .filter(job -> job.getDescription() != null
                           && skills.stream().anyMatch(skill -> job.getDescription().toLowerCase().contains(skill.toLowerCase())))
                   .collect(Collectors.toList());
       }

        if (skills != null && !skills.isEmpty() && !jobs.isEmpty()) {
            jobs = aiService.rankJobs(jobs, skills);
        }

       return jobs;
    }

    private boolean matchesLocation(JobDto job, String normalizedLocation, String countryCode) {
        if (job.getLocation() == null) {
            return false;
        }

        String jobLocation = job.getLocation().toLowerCase();
        boolean matchesUserLocation = !normalizedLocation.isBlank() && jobLocation.contains(normalizedLocation);
        boolean matchesCountryCode = countryCode != null && jobLocation.contains(countryCode);

        return matchesUserLocation || matchesCountryCode;
    }

    private String resolveCountryCode(String location) {
        if (location == null || location.isBlank()) {
            return null;
        }

        String[] parts = location.split(",");

        for (String rawPart : parts) {
            String part = rawPart.trim();
            if (part.length() == 2 && part.chars().allMatch(Character::isLetter)) {
                return part.toLowerCase();
            }

            for (String isoCountry : Locale.getISOCountries()) {
                Locale locale = Locale.of("", isoCountry);
                if (locale.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(part)) {
                    return isoCountry.toLowerCase();
                }
            }
        }

        return null;
    }

}
