package com.ats.analyzer.logic;

import com.ats.analyzer.model.RoleIntent;

import java.util.HashSet;
import java.util.Set;

/**
 * Classifies skills as CORE (critical) vs SECONDARY (nice-to-have).
 * Used for importance-weighted scoring.
 */
public class SkillClassifier {

    // Core skills for technical roles (must-have)
    private static final Set<String> CORE_TECH_SKILLS = Set.of(
            "python", "java", "javascript", "sql", "git",
            "programming", "coding", "algorithm", "dsa",
            "api", "database", "backend", "frontend");

    // Core skills for AI/ML roles
    private static final Set<String> CORE_AI_SKILLS = Set.of(
            "python", "ml", "ai", "sql", "tensorflow", "pytorch",
            "pandas", "numpy", "data", "model", "algorithm");

    // Core skills for business roles
    private static final Set<String> CORE_BUSINESS_SKILLS = Set.of(
            "communication", "sales", "negotiation", "excel",
            "presentation", "crm", "analytical", "business-development");

    // Core skills for marketing roles (note: google-analytics, not generic
    // "analytics")
    private static final Set<String> CORE_MARKETING_SKILLS = Set.of(
            "marketing", "seo", "content", "google-analytics",
            "communication", "email-marketing", "digital-marketing");

    // Core skills for UI/UX design roles (NOT development)
    private static final Set<String> CORE_DESIGN_SKILLS = Set.of(
            "figma", "adobe-xd", "sketch", "user-experience", "user-interface",
            "wireframing", "prototyping", "user-research", "usability",
            "design-systems", "visual-design");

    /**
     * Identify which skills are CORE (critical) based on role intent.
     * 
     * @param skills     Set of skills to classify
     * @param roleIntent Job role type
     * @return Subset of skills that are CORE for this role
     */
    public static Set<String> identifyCoreSkills(Set<String> skills, RoleIntent roleIntent) {
        Set<String> coreSkills = new HashSet<>();

        for (String skill : skills) {
            if (isCoreSkill(skill, roleIntent)) {
                coreSkills.add(skill);
            }
        }

        return coreSkills;
    }

    /**
     * Check if a skill is CORE for a given role type.
     */
    private static boolean isCoreSkill(String skill, RoleIntent roleIntent) {
        String normalized = skill.toLowerCase();

        // SPECIAL CASE: HTML/CSS are NOT core for design/marketing roles
        // They're frontend dev skills, not designer skills
        if ((normalized.equals("html") || normalized.equals("css")) &&
                roleIntent == RoleIntent.TECH_ADJACENT) {
            return false; // Not core for design/marketing roles
        }

        return switch (roleIntent) {
            case TECH_CORE -> {
                // Check if it's a core tech OR core AI skill
                if (CORE_TECH_SKILLS.contains(normalized) || CORE_AI_SKILLS.contains(normalized)) {
                    yield true;
                }
                yield false;
            }
            case TECH_ADJACENT -> {
                // Marketing/Product/Design roles: mix of marketing, business, AND design skills
                yield CORE_MARKETING_SKILLS.contains(normalized) ||
                        CORE_BUSINESS_SKILLS.contains(normalized) ||
                        CORE_DESIGN_SKILLS.contains(normalized);
            }
            case NON_TECH -> {
                // Business/Sales roles: focus on soft skills
                yield CORE_BUSINESS_SKILLS.contains(normalized);
            }
        };
    }
}
