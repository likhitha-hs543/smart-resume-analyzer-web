package com.ats.analyzer.logic;

import com.ats.analyzer.model.RoleIntent;

import java.util.HashSet;
import java.util.Set;

/**
 * Classifies skills as CORE (critical) vs SECONDARY (nice-to-have).
 * CRITICAL: Core = programming fundamentals, NOT frameworks/tools.
 */
public class SkillClassifier {

    // Core skills for technical roles = FUNDAMENTALS ONLY
    // Frameworks/tools (Spring, Docker, AWS) are NOT core
    private static final Set<String> CORE_TECH_SKILLS = Set.of(
            // Programming languages (fundamentals)
            "python", "java", "javascript", "typescript", "c", "cpp",
            "csharp", "go", "ruby", "php", "rust", "kotlin", "scala",

            // CS fundamentals
            "dsa", "data-structures", "algorithms", "programming", "coding",

            // Database fundamentals
            "sql", "database",

            // Version control
            "git",

            // API fundamentals
            "api", "rest");

    // Core skills for AI/ML roles = fundamentals + key libraries
    private static final Set<String> CORE_AI_SKILLS = Set.of(
            "python", "ml", "ai", "sql", "tensorflow", "pytorch",
            "pandas", "numpy", "algorithms", "data");

    // Core skills for business roles
    private static final Set<String> CORE_BUSINESS_SKILLS = Set.of(
            "communication", "sales", "negotiation", "excel",
            "presentation", "crm", "analytical", "business-development");

    // Core skills for marketing roles
    private static final Set<String> CORE_MARKETING_SKILLS = Set.of(
            "marketing", "seo", "content", "google-analytics",
            "communication", "email-marketing", "digital-marketing");

    // Core skills for UI/UX design roles
    private static final Set<String> CORE_DESIGN_SKILLS = Set.of(
            "figma", "adobe-xd", "sketch", "user-experience", "user-interface",
            "wireframing", "prototyping", "user-research", "usability",
            "design-systems", "visual-design");

    /**
     * Identify which skills are CORE (critical) based on role intent.
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

        // EXPLICIT EXCLUSIONS: Frameworks/tools are NEVER core (regardless of role)
        Set<String> neverCore = Set.of(
                "spring", "springboot", "nodejs", "node", "react", "angular", "vue",
                "docker", "kubernetes", "aws", "azure", "gcp", "microservices",
                "jenkins", "gitlab", "terraform", "ansible", "kafka", "redis",
                "mongodb", "postgresql", "mysql", "flask", "django", "express",
                "fastapi", "dotnet", "elasticsearch", "rabbitmq", "nginx", "apache",
                "cicd", "ci-cd", "devops", "jira", "confluence", "grafana", "prometheus");

        if (neverCore.contains(normalized)) {
            return false; // Frameworks/tools are secondary, not core
        }

        // SPECIAL CASE: HTML/CSS are NOT core for design roles
        if ((normalized.equals("html") || normalized.equals("css")) &&
                roleIntent == RoleIntent.TECH_ADJACENT) {
            return false;
        }

        return switch (roleIntent) {
            case TECH_CORE -> {
                // Only programming languages, DSA, Git, SQL, API fundamentals
                if (CORE_TECH_SKILLS.contains(normalized) || CORE_AI_SKILLS.contains(normalized)) {
                    yield true;
                }
                yield false;
            }
            case TECH_ADJACENT -> {
                // Marketing/Product/Design roles
                yield CORE_MARKETING_SKILLS.contains(normalized) ||
                        CORE_BUSINESS_SKILLS.contains(normalized) ||
                        CORE_DESIGN_SKILLS.contains(normalized);
            }
            case NON_TECH -> {
                // Business/Sales roles: soft skills only
                yield CORE_BUSINESS_SKILLS.contains(normalized);
            }
        };
    }
}
