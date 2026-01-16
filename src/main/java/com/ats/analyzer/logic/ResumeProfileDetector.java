package com.ats.analyzer.logic;

import com.ats.analyzer.model.ResumeProfile;

/**
 * Detects resume background profile based on technical signal count.
 * Used for role compatibility scoring.
 */
public class ResumeProfileDetector {

    private static final String[] TECH_SIGNALS = {
            "python", "java", "ml", "ai", "sql", "api", "github",
            "project", "javascript", "programming", "code", "software",
            "algorithm", "data structure", "backend", "frontend", "devops"
    };

    /**
     * Detects resume profile from resume text.
     * 
     * @param resumeText Full resume content
     * @return Detected ResumeProfile
     */
    public static ResumeProfile detect(String resumeText) {
        String text = resumeText.toLowerCase();

        int techSignals = count(text, TECH_SIGNALS);

        // High technical signal count = technical profile
        if (techSignals >= 5) {
            return ResumeProfile.TECHNICAL;
        }

        // Some technical signals = mixed profile
        if (techSignals >= 2) {
            return ResumeProfile.MIXED;
        }

        // Minimal technical signals = non-tech profile
        return ResumeProfile.NON_TECH;
    }

    private static int count(String text, String[] keywords) {
        int count = 0;
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                count++;
            }
        }
        return count;
    }
}
