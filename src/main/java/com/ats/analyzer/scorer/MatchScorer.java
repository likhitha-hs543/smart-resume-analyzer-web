package com.ats.analyzer.scorer;

import com.ats.analyzer.logic.RoleIntentDetector;
import com.ats.analyzer.logic.SkillWeightingPolicy;
import com.ats.analyzer.model.RoleIntent;

import java.util.Set;

/**
 * RoleIntent-aware ATS scoring engine.
 * Considers job type (technical vs business) and applies appropriate skill
 * weighting.
 * Produces realistic scores (no 0% or 100% extremes).
 */
public class MatchScorer {

    /**
     * Calculate RoleIntent-aware ATS match score.
     * 
     * @param matchedSkills  Skills present in both resume and JD
     * @param missingSkills  Skills in JD but not in resume
     * @param extraSkills    Skills in resume but not in JD
     * @param jobDescription Full job description text for intent detection
     * @return ATS match score (10-95%)
     */
    public static double calculateScore(
            Set<String> matchedSkills,
            Set<String> missingSkills,
            Set<String> extraSkills,
            String jobDescription) {

        // 1. Detect role intent from JD
        RoleIntent intent = RoleIntentDetector.detect(jobDescription);

        // 2. Calculate total relevant skills
        int totalRelevant = matchedSkills.size() + missingSkills.size();
        if (totalRelevant == 0) {
            return SkillWeightingPolicy.minimumScoreFloor(intent);
        }

        // 3. Calculate base match rate
        double matchRate = (double) matchedSkills.size() / totalRelevant;

        // 4. Apply role-specific core skill weight
        double coreScore = matchRate * SkillWeightingPolicy.coreSkillWeight(intent);

        // 5. Calculate and apply missing skill penalty
        double missingRate = (double) missingSkills.size() / totalRelevant;
        double penalty = missingRate * SkillWeightingPolicy.toolPenalty(intent);

        // 6. Calculate raw score
        double rawScore = (coreScore - penalty) * 100;

        // 7. Apply minimum floor and cap at 95% (no perfect scores)
        int finalScore = (int) Math.round(rawScore);
        finalScore = Math.max(finalScore, SkillWeightingPolicy.minimumScoreFloor(intent));
        finalScore = Math.min(finalScore, 95);

        return finalScore;
    }
}
