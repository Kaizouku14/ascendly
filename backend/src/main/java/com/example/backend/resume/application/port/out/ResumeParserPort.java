package com.example.backend.resume.application.port.out;

import com.example.backend.resume.dto.ResumeDto;

public interface ResumeParserPort {
    ResumeDto parse(String resumeText);
}
