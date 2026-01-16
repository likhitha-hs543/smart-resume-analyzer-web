package com.ats.analyzer.model;

import java.util.Map;
import java.util.Set;

/**
 * Defines synonym relationships between skills.
 * CRITICAL: Keeps tool skills (analytics platforms) separate from soft skills
 * (analytical thinking).
 */
public class SkillRelation {

    /**
     * Synonym groups - skills within a group are considered equivalent.
     */
    private static final Map<String, Set<String>> SYNONYM_GROUPS = Map.ofEntries(
            // Version control
            Map.entry("git", Set.of("git", "github", "gitlab")),

            // AI/ML
            Map.entry("ml", Set.of("ml", "machine-learning", "machine learning")),
            Map.entry("ai", Set.of("ai", "artificial-intelligence", "artificial intelligence")),
            Map.entry("nlp", Set.of("nlp", "natural-language-processing", "natural language processing")),

            // Office tools
            Map.entry("excel", Set.of("excel", "ms-excel", "microsoft-excel", "spreadsheet", "sheets")),
            Map.entry("powerpoint", Set.of("powerpoint", "ppt", "ms-powerpoint", "presentation", "slides")),
            Map.entry("word", Set.of("word", "ms-word", "microsoft-word")),

            // Methodologies
            Map.entry("agile", Set.of("agile", "scrum", "kanban")),

            // Programming languages
            Map.entry("javascript", Set.of("javascript", "js", "node")),
            Map.entry("python", Set.of("python", "py")),

            // Databases
            Map.entry("sql", Set.of("sql", "mysql", "postgresql", "postgres")),

            // Cloud
            Map.entry("aws", Set.of("aws", "amazon-web-services")),
            Map.entry("azure", Set.of("azure", "microsoft-azure")),
            Map.entry("gcp", Set.of("gcp", "google-cloud", "google-cloud-platform")),

            // CI/CD
            Map.entry("cicd", Set.of("cicd", "ci-cd", "ci/cd", "continuous-integration")),

            // Data structures
            Map.entry("dsa", Set.of("dsa", "data-structures", "data-structures-algorithms")));

    /**
     * Skills that sound similar but have DIFFERENT meanings.
     * These should NEVER match each other.
     */
    private static final Set<String> ANALYTICS_TOOLS = Set.of(
            "analytics", "google-analytics", "data-analytics",
            "web-analytics", "business-analytics", "tableau-analytics");

    private static final Set<String> ANALYTICAL_SKILLS = Set.of(
            "analytical", "analytical-thinking", "analytical-skills",
            "critical-thinking", "problem-solving");

    /**
     * Check if two skills are related (synonyms or same group).
     */
    public static boolean areRelated(String skill1, String skill2) {
        if (skill1.equalsIgnoreCase(skill2)) {
            return true;
        }

        String s1 = skill1.toLowerCase();
        String s2 = skill2.toLowerCase();

        // CRITICAL: Prevent analytics (tool) from matching analytical (mindset)
        if ((ANALYTICS_TOOLS.contains(s1) && ANALYTICAL_SKILLS.contains(s2)) ||
                (ANALYTICAL_SKILLS.contains(s1) && ANALYTICS_TOOLS.contains(s2))) {
            return false;
        }

        // Check if both skills belong to the same synonym group
        for (Set<String> group : SYNONYM_GROUPS.values()) {
            if (group.contains(s1) && group.contains(s2)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the canonical (primary) form of a skill.
     */
    public static String getCanonical(String skill) {
        String lowerSkill = skill.toLowerCase();

        for (Map.Entry<String, Set<String>> entry : SYNONYM_GROUPS.entrySet()) {
            if (entry.getValue().contains(lowerSkill)) {
                return entry.getKey();
            }
        }

        return lowerSkill;
    }
}
