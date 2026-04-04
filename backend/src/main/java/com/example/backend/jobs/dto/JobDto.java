package com.example.backend.jobs.dto;

import lombok.Data;

@Data
public class JobDto {
    private String jobId;
    private String title;
    private String company;
    private String location;
    private String description;
    private String applyLink;
    private String employmentType;
    private Boolean isRemote;
}
