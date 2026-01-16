package com.ats.analyzer.logic;

import com.ats.analyzer.model.RoleIntent;

import java.util.Set;

/**
 * Detects job role intent from job description text.
 * Uses keyword matching to classify roles into TECH_CORE, TECH_ADJACENT, or
 * NON_TECH.
 */
public class RoleIntentDetector {

    private static final Set<String> TECH_CORE_KEYWORDS = Set.of(
            "software", "developer", "engineer", "ai", "machine learning",
            "data scientist", "devops", "backend", "frontend", "sde",
            "programming", "coding", "algorithm", "infrastructure",
            "cloud engineer", "ml engineer", "full stack");

    private static final Set<String> TECH_ADJACENT_KEYWORDS = Set.of(
            "marketing", "business", "growth", "analyst", "operations",
            "strategy", "content", "seo", "digital marketing", "product manager",
            "business development", "sales engineer", "technical writer");

    /**
     * Detects role intent from job description text.
     * 
     * @param jobDescription Full text of job description
     * @return Detected RoleIntent
     */
    public static RoleIntent detect(String jobDescription) {
        String jd = jobDescription.toLowerCase();

        if (containsAny(jd, TECH_CORE_KEYWORDS)) {
            return RoleIntent.TECH_CORE;
        }

        if (containsAny(jd, TECH_ADJACENT_KEYWORDS)) {
            return RoleIntent.TECH_ADJACENT;
        }

        return RoleIntent.NON_TECH;
    }

    private static boolean containsAny(String text, Set<String> keywords) {
        return keywords.stream().anyMatch(text::contains);
    }
}
