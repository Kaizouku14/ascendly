package com.example.backend.resume;

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
  private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResumeDto parseResume(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            throw new RuntimeException("No file uploaded.");
        }

        String extension = file.getOriginalFilename();
        if (extension == null || !extension.endsWith(".pdf")) {
            throw new RuntimeException("Only PDF files are accepted.");
        }

        String text = resumeService.extractTextFromPdf(file);
        return resumeService.parseWithGroq(text);
    }
}
