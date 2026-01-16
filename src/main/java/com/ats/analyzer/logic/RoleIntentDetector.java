package com.ats.analyzer.logic;

import com.ats.analyzer.model.RoleIntent;

import java.util.Set;

/**
 * Detects job role intent from job description text.
 * Uses keyword matching with threshold-based classification and design role
 * detection.
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
            "business development", "sales engineer", "technical writer",
            // Design roles
            "ui/ux", "ux", "ui", "designer", "design", "figma",
            "user experience", "user interface", "wireframe", "prototype",
            // Data/Analytics roles
            "data analyst", "business analyst");

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

    /**
     * Check if this is specifically a UI/UX design role (not frontend development).
     * Used to apply special scoring penalties for dev→design mismatches.
     * 
     * @param jobDescription Full text of job description
     * @return true if this is a pure design role
     */
    public static boolean isDesignRole(String jobDescription) {
        String jd = jobDescription.toLowerCase();

        // Design role indicators
        int designScore = 0;
        if (jd.contains("ui/ux") || jd.contains("ux designer") || jd.contains("ui designer")) {
            designScore += 3;
        }
        if (jd.contains("figma") || jd.contains("adobe xd") || jd.contains("sketch")) {
            designScore += 2;
        }
        if (jd.contains("wireframe") || jd.contains("prototype") || jd.contains("user research")) {
            designScore += 1;
        }

        // Development indicators (these override design classification)
        int devScore = 0;
        if (jd.contains("react") || jd.contains("vue") || jd.contains("angular")) {
            devScore += 2;
        }
        if (jd.contains("frontend development") || jd.contains("javascript framework")) {
            devScore += 2;
        }

        // It's a design role if design score is high and dev score is low
        return designScore >= 3 && devScore < 2;
    }

    private static int countMatches(String text, Set<String> keywords) {
        return (int) keywords.stream().filter(text::contains).count();
    }
}
