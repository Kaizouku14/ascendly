package com.example.backend.resume.web;

import com.example.backend.resume.application.ResumeParsingUseCase;
import com.example.backend.resume.dto.ResumeDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeParsingUseCase resumeParsingUseCase;

    public ResumeController(ResumeParsingUseCase resumeParsingUseCase) {
        this.resumeParsingUseCase = resumeParsingUseCase;
    }

    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResumeDto parseResume(@RequestParam("file") MultipartFile file) {
        return resumeParsingUseCase.parse(file);
    }
}
