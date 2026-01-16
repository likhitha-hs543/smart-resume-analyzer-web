package com.ats.analyzer.matcher;

import com.ats.analyzer.model.SkillRelation;

import java.util.HashSet;
import java.util.Set;

/**
 * Skill matching engine using set operations with synonym support.
 * Compares resume skills against job description requirements.
 */
public class SkillMatcher {

    /**
     * Match resume skills against JD requirements with synonym awareness.
     * 
     * @param resumeSkills Skills extracted from resume
     * @param jdSkills     Skills extracted from job description
     * @return MatchResult containing matched, missing, and extra skills
     */
    public static MatchResult matchSkills(Set<String> resumeSkills, Set<String> jdSkills) {

        // Normalize skills to canonical forms
        Set<String> normalizedResume = normalizeSkills(resumeSkills);
        Set<String> normalizedJD = normalizeSkills(jdSkills);

        // Matched: resume ∩ JD (with synonym matching)
        Set<String> matched = new HashSet<>();
        for (String jdSkill : normalizedJD) {
            if (normalizedResume.contains(jdSkill)) {
                matched.add(jdSkill);
            }
        }

        // Missing: JD - resume (with synonym matching)
        Set<String> missing = new HashSet<>();
        for (String jdSkill : normalizedJD) {
            if (!normalizedResume.contains(jdSkill)) {
                missing.add(jdSkill);
            }
        }

        // Extra: resume - JD (with synonym matching)
        Set<String> extra = new HashSet<>();
        for (String resumeSkill : normalizedResume) {
            if (!normalizedJD.contains(resumeSkill)) {
                extra.add(resumeSkill);
            }
        }

        return new MatchResult(matched, missing, extra);
    }

    /**
     * Normalize skills to their canonical forms.
     * Example: "github" → "git", "machine learning" → "ml"
     */
    private static Set<String> normalizeSkills(Set<String> skills) {
        Set<String> normalized = new HashSet<>();
        for (String skill : skills) {
            normalized.add(SkillRelation.getCanonical(skill));
        }
        return normalized;
    }
}
