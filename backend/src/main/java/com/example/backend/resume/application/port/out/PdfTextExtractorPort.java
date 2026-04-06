package com.example.backend.resume.application.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface PdfTextExtractorPort {
    String extractText(MultipartFile file);
}
