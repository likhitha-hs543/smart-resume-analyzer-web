package com.ats.analyzer.service;

import com.ats.analyzer.extractor.KeywordExtractor;
import com.ats.analyzer.matcher.MatchResult;
import com.ats.analyzer.matcher.SkillMatcher;
import com.ats.analyzer.parser.TextCleaner;
import com.ats.analyzer.scorer.MatchScorer;
import com.ats.analyzer.suggestion.SuggestionEngine;
import com.ats.web.dto.AnalysisResult;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@Service
public class AnalyzerService {

    public AnalysisResult analyze(MultipartFile resumeFile, String jobDescription) {

        if (resumeFile == null || resumeFile.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required");
        }

        if (jobDescription == null || jobDescription.isBlank()) {
            throw new IllegalArgumentException("Job description is required");
        }

        try {
            // 1. Extract resume text from MultipartFile
            String resumeText = extractResumeText(resumeFile);

            // 2. Load JD text (pass as string)
            String jdText = jobDescription;

            // 3. Clean both texts
            String cleanResume = TextCleaner.clean(resumeText);
            String cleanJD = TextCleaner.clean(jdText);

            // 4. Extract skills
            Set<String> resumeSkills = KeywordExtractor.extractSkills(cleanResume);
            Set<String> jdSkills = KeywordExtractor.extractSkills(cleanJD);

            // 5. Match skills
            MatchResult matchResult = SkillMatcher.matchSkills(resumeSkills, jdSkills);

            // 6. Calculate domain-aware score
            double score = MatchScorer.calculateScore(
                    matchResult.getMatchedSkills(),
                    jdSkills,
                    resumeSkills // For domain detection
            );

            // 7. Generate suggestions
            List<String> suggestions = SuggestionEngine.generateSuggestions(
                    matchResult.getMissingSkills(),
                    matchResult.getExtraSkills(),
                    score);

            return new AnalysisResult(
                    score,
                    matchResult.getMatchedSkills(),
                    matchResult.getMissingSkills(),
                    matchResult.getExtraSkills(),
                    suggestions);

        } catch (IOException e) {
            throw new RuntimeException("Error processing resume file: " + e.getMessage(), e);
        }
    }

    /**
     * Extract text from uploaded MultipartFile (PDF or TXT)
     */
    private String extractResumeText(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new IOException("File name is missing");
        }

        fileName = fileName.toLowerCase();

        if (fileName.endsWith(".pdf")) {
            return extractFromPDF(file);
        } else if (fileName.endsWith(".txt")) {
            return extractFromText(file);
        } else {
            throw new IOException("Unsupported file format. Please provide .pdf or .txt file.");
        }
    }

    private String extractFromPDF(MultipartFile pdfFile) throws IOException {
        try (InputStream inputStream = pdfFile.getInputStream();
                PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractFromText(MultipartFile textFile) throws IOException {
        return new String(textFile.getBytes(), StandardCharsets.UTF_8);
    }
}
