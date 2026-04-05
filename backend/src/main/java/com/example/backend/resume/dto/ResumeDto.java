package com.example.backend.resume.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResumeDto {
    private String fullName;
    private String email;
    private String phone;
    private String location;
    private String summary;
    private String targetRole;
    private List<String> skills;
    private List<String> education;
    private List<String> experience;
}

