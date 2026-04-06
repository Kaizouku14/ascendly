package com.example.backend.resume.application;

import com.example.backend.common.exception.InvalidRequestException;
import com.example.backend.resume.application.port.out.PdfTextExtractorPort;
import com.example.backend.resume.application.port.out.ResumeParserPort;
import com.example.backend.resume.dto.ResumeDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeParsingService implements ResumeParsingUseCase {

    private final PdfTextExtractorPort pdfTextExtractor;
    private final ResumeParserPort resumeParser;

    public ResumeParsingService(PdfTextExtractorPort pdfTextExtractor, ResumeParserPort resumeParser) {
        this.pdfTextExtractor = pdfTextExtractor;
        this.resumeParser = resumeParser;
    }

    @Override
    public ResumeDto parse(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidRequestException("No file uploaded.");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.endsWith(".pdf")) {
            throw new InvalidRequestException("Only PDF files are accepted.");
        }

        String text = pdfTextExtractor.extractText(file);
        return resumeParser.parse(text);
    }
}
