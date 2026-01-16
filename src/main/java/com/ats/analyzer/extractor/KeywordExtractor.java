package com.ats.analyzer.extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Skill extraction with whitelist filtering.
 * Differentiates actual technical skills from generic words like "experience",
 * "responsible".
 * 
 * Known limitation (intentional MVP constraint):
 * - No synonym handling: "js" vs "javascript", "springboot" vs "spring boot"
 */
public class KeywordExtractor {

    // Cached skill whitelist loaded once from resources
    private static final Set<String> SKILL_WHITELIST = loadSkillWhitelist();

    // Hardcoded stop words list (common words + HR/business terms)
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "and", "are", "as", "at", "be", "by", "for",
            "from", "has", "he", "in", "is", "it", "its", "of", "on",
            "that", "the", "to", "was", "will", "with", "the", "this",
            "but", "they", "have", "had", "what", "when", "where", "who",
            "which", "why", "how", "all", "each", "every", "both", "few",
            "more", "most", "other", "some", "such", "no", "nor", "not",
            "only", "own", "same", "so", "than", "too", "very", "can",
            "will", "just", "should", "now", "experience", "work", "working",
            "development", "developer", "project", "projects", "responsible",
            "responsibilities", "using", "used", "including", "ability",
            "knowledge", "understanding", "years", "months", "skills",
            "skill", "strong", "good", "excellent", "proficient",
            // Added: common business/HR terms that aren't skills
            "conversion", "performance", "familiarity", "pursuing", "completed",
            "degree", "bachelor", "master", "internship", "full-time", "part-time",
            "fresher", "candidate", "applicant", "required", "preferred", "mandatory",
            "responsibility", "qualification", "opportunity", "benefit", "package"));

    /**
     * Extract skills from resume text
     */
    public static Set<String> extractSkills(String cleanedText) {
        Set<String> skills = new HashSet<>();

        if (cleanedText == null || cleanedText.trim().isEmpty()) {
            return skills;
        }

        // Split into tokens
        String[] tokens = cleanedText.split("\\s+");

        for (String token : tokens) {
            // Skip if too short
            if (token.length() < 2) {
                continue;
            }

            // Skip if stop word
            if (STOP_WORDS.contains(token)) {
                continue;
            }

            // Only keep if in skill whitelist
            if (SKILL_WHITELIST.contains(token)) {
                skills.add(token);
            }
        }

        return skills;
    }

    /**
     * Load skill whitelist from resources (cached)
     */
    private static Set<String> loadSkillWhitelist() {
        Set<String> skills = new HashSet<>();

        try (InputStream is = KeywordExtractor.class.getResourceAsStream("/skills.txt")) {
            if (is == null) {
                System.err.println("Warning: skills.txt not found in resources, using empty whitelist");
                return skills;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            skills = reader.lines()
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toSet());

        } catch (IOException e) {
            System.err.println("Error loading skills.txt: " + e.getMessage());
        }

        return skills;
    }
}
