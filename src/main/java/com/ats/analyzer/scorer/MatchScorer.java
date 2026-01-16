package com.ats.analyzer.scorer;

import com.ats.analyzer.logic.CompatibilityMatrix;
import com.ats.analyzer.logic.ResumeProfileDetector;
import com.ats.analyzer.logic.RoleIntentDetector;
import com.ats.analyzer.logic.SkillWeightingPolicy;
import com.ats.analyzer.model.ResumeProfile;
import com.ats.analyzer.model.RoleIntent;

import java.util.Set;

/**
 * Complete ATS scoring engine with RoleIntent and ResumeProfile compatibility.
 * Produces realistic scores that consider both skill match AND role
 * plausibility.
 */
public class MatchScorer {

    /**
     * Calculate complete ATS match score with role compatibility.
     * 
     * @param matchedSkills  Skills present in both resume and JD
     * @param missingSkills  Skills in JD but not in resume
     * @param extraSkills    Skills in resume but not in JD
     * @param jobDescription Full job description text
     * @param resumeText     Full resume text
     * @return ATS match score (10-95%)
     */
    public static double calculateScore(
            Set<String> matchedSkills,
            Set<String> missingSkills,
            Set<String> extraSkills,
            String jobDescription,
            String resumeText) {

        // 1. Detect role intent and resume profile
        RoleIntent roleIntent = RoleIntentDetector.detect(jobDescription);
        ResumeProfile resumeProfile = ResumeProfileDetector.detect(resumeText);

        // 2. Calculate total relevant skills
        int totalRelevant = matchedSkills.size() + missingSkills.size();
        if (totalRelevant == 0) {
            return CompatibilityMatrix.minimumFloor(roleIntent);
        }

        // 3. Calculate base skill match rate
        double matchRate = (double) matchedSkills.size() / totalRelevant;

        // 4. Apply role-specific skill weight
        double coreScore = matchRate * SkillWeightingPolicy.coreSkillWeight(roleIntent);

        // 5. Calculate missing skill penalty
        double missingRate = (double) missingSkills.size() / totalRelevant;
        double penalty = missingRate * SkillWeightingPolicy.toolPenalty(roleIntent);

        // 6. Calculate skill-only score
        double skillScore = (coreScore - penalty) * 100;

        // 7. Apply role×resume compatibility multiplier (THE KEY FIX)
        double compatibilityFactor = CompatibilityMatrix.compatibilityMultiplier(
                roleIntent, resumeProfile);
        double adjustedScore = skillScore * compatibilityFactor;

        // 8. Apply minimum floor and cap at 95%
        int finalScore = (int) Math.round(adjustedScore);
        finalScore = Math.max(finalScore, CompatibilityMatrix.minimumFloor(roleIntent));
        finalScore = Math.min(finalScore, 95);

        return finalScore;
    }
}
