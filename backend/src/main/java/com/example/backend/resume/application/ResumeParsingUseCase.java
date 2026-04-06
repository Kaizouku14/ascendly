package com.example.backend.resume.application;

import com.example.backend.resume.dto.ResumeDto;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeParsingUseCase {
    ResumeDto parse(MultipartFile file);
}
