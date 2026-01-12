package com.ats.analyzer.matcher;

import java.util.HashSet;
import java.util.Set;

/**
 * Skill matching engine using set operations.
 * Compares resume skills against job description requirements.
 */
public class SkillMatcher {

    /**
     * Match resume skills against JD requirements
     * 
     * @param resumeSkills Skills extracted from resume
     * @param jdSkills     Skills extracted from job description
     * @return MatchResult containing matched, missing, and extra skills
     */
    public static MatchResult matchSkills(Set<String> resumeSkills, Set<String> jdSkills) {
        // Matched: resume ∩ JD
        Set<String> matched = new HashSet<>(resumeSkills);
        matched.retainAll(jdSkills);

        // Missing: JD - resume
        Set<String> missing = new HashSet<>(jdSkills);
        missing.removeAll(resumeSkills);

        // Extra: resume - JD
        Set<String> extra = new HashSet<>(resumeSkills);
        extra.removeAll(jdSkills);

        return new MatchResult(matched, missing, extra);
    }
}
