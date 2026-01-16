package com.ats.analyzer.logic;

import com.ats.analyzer.model.RoleIntent;

import java.util.Set;

/**
 * Detects job role intent from job description text.
 * Uses keyword matching with threshold-based classification to prevent
 * misclassification.
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
     * Uses prioritized keyword matching with conflict resolution.
     * 
     * @param jobDescription Full text of job description
     * @return Detected RoleIntent
     */
    public static RoleIntent detect(String jobDescription) {
        String jd = jobDescription.toLowerCase();

        // Count matches for each category
        int techCoreCount = countMatches(jd, TECH_CORE_KEYWORDS);
        int techAdjacentCount = countMatches(jd, TECH_ADJACENT_KEYWORDS);

        // Decision logic with threshold
        // If primarily tech keywords → TECH_CORE
        if (techCoreCount >= 3 && techCoreCount > techAdjacentCount * 2) {
            return RoleIntent.TECH_CORE;
        }

        // If mixed but more adjacent → TECH_ADJACENT
        if (techAdjacentCount >= 2) {
            return RoleIntent.TECH_ADJACENT;
        }

        // If some tech keywords but not dominant → TECH_ADJACENT
        if (techCoreCount >= 2 && techCoreCount <= 4) {
            return RoleIntent.TECH_ADJACENT;
        }

        // If strong tech core keywords → TECH_CORE
        if (techCoreCount >= 5) {
            return RoleIntent.TECH_CORE;
        }

        // Default to NON_TECH
        return RoleIntent.NON_TECH;
    }

    private static int countMatches(String text, Set<String> keywords) {
        return (int) keywords.stream().filter(text::contains).count();
    }
}
