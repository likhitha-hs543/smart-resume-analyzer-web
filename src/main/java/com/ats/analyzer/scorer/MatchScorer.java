package com.ats.analyzer.scorer;

import java.util.Set;

/**
 * ATS-style scoring engine.
 * 
 * Edge cases (documented as design choices):
 * 1. If JD has 0 skills → return 0
 * 2. If JD has very few skills (1-2) → score swings wildly (50% jump per skill)
 * 3. If resume matches 100% + has extras → still scores 100 (extras don't
 * penalize)
 * 
 * Design rationale:
 * - Formula prioritizes JD requirements coverage (employer perspective)
 * - Extra skills are surfaced separately, not penalized in score
 * - Small JD datasets are user error, not system bug
 */
public class MatchScorer {

    /**
     * Calculate ATS match percentage
     * 
     * @param matchedSkills Skills present in both resume and JD
     * @param jdSkills      All skills from job description
     * @return Match percentage (0-100)
     */
    public static double calculateScore(Set<String> matchedSkills, Set<String> jdSkills) {
        if (jdSkills == null || jdSkills.isEmpty()) {
            return 0.0;
        }

        double score = ((double) matchedSkills.size() / jdSkills.size()) * 100;
        return Math.min(score, 100.0); // Cap at 100
    }
}
