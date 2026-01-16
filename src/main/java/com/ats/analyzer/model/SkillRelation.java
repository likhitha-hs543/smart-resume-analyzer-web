package com.ats.analyzer.model;

import java.util.Map;
import java.util.Set;

/**
 * Defines synonym relationships between skills.
 * Prevents penalizing users for listing "github" when JD says "git".
 */
public class SkillRelation {

    /**
     * Synonym groups - skills within a group are considered equivalent.
     */
    private static final Map<String, Set<String>> SYNONYM_GROUPS = Map.ofEntries(
            // Version control
            Map.entry("git", Set.of("git", "github", "gitlab")),

            // AI/ML
            Map.entry("ml", Set.of("ml", "machine learning", "machine-learning")),
            Map.entry("ai", Set.of("ai", "artificial intelligence", "artificial-intelligence")),
            Map.entry("nlp", Set.of("nlp", "natural language processing")),

            // Office tools
            Map.entry("excel", Set.of("excel", "spreadsheet", "sheets", "ms-excel")),
            Map.entry("powerpoint", Set.of("powerpoint", "ppt", "presentation", "slides")),

            // Methodologies
            Map.entry("agile", Set.of("agile", "scrum", "kanban")),

            // Programming languages (common variations)
            Map.entry("javascript", Set.of("javascript", "js", "node")),
            Map.entry("python", Set.of("python", "py")),

            // Databases
            Map.entry("sql", Set.of("sql", "mysql", "postgresql", "postgres")),

            // Cloud
            Map.entry("aws", Set.of("aws", "amazon-web-services")),
            Map.entry("azure", Set.of("azure", "microsoft-azure")),
            Map.entry("gcp", Set.of("gcp", "google-cloud", "google-cloud-platform")));

    /**
     * Check if two skills are related (synonyms or same group).
     * 
     * @param skill1 First skill
     * @param skill2 Second skill
     * @return true if skills are related, false otherwise
     */
    public static boolean areRelated(String skill1, String skill2) {
        if (skill1.equalsIgnoreCase(skill2)) {
            return true;
        }

        // Check if both skills belong to the same synonym group
        for (Set<String> group : SYNONYM_GROUPS.values()) {
            if (group.contains(skill1.toLowerCase()) && group.contains(skill2.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Find the canonical (primary) form of a skill.
     * 
     * @param skill Skill name
     * @return Canonical form, or original if no match
     */
    public static String getCanonical(String skill) {
        String lowerSkill = skill.toLowerCase();

        for (Map.Entry<String, Set<String>> entry : SYNONYM_GROUPS.entrySet()) {
            if (entry.getValue().contains(lowerSkill)) {
                return entry.getKey(); // Return the canonical form
            }
        }

        return lowerSkill; // Return as-is if not found
    }
}
