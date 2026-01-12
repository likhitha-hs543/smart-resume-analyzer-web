package com.ats.web.controller;

import com.ats.analyzer.service.AnalyzerService;
import com.ats.web.dto.AnalysisResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class AnalyzerController {

    private final AnalyzerService analyzerService;

    public AnalyzerController(AnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AnalysisResult analyze(
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("jobDescription") String jobDescription) {
        System.out.println(">>> CONTROLLER HIT");
        System.out.println("File name: " + resume.getOriginalFilename());
        System.out.println("File size: " + resume.getSize());
        return analyzerService.analyze(resume, jobDescription);
    }
}
