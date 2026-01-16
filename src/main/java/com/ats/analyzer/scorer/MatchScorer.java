package com.ats.analyzer.scorer;

import com.ats.analyzer.logic.CompatibilityMatrix;
import com.ats.analyzer.logic.ResumeProfileDetector;
import com.ats.analyzer.logic.RoleIntentDetector;
import com.ats.analyzer.model.ResumeProfile;
import com.ats.analyzer.model.RoleIntent;

import java.util.Set;

/**
 * Final ATS scoring engine with domain-aware logic.
 * Produces realistic scores (10-95%) that consider both skill match AND role
 * compatibility.
 * 
 * Design Philosophy:
 * - Deterministic (no ML)
 * - Explainable (rule-based)
 * - Realistic (no 0% or 100% extremes)
 */
public class MatchScorer {

    /**
     * Calculate complete ATS match score with role compatibility.
     * 
     * @param matchedSkills  Skills present in both resume and JD
     * @param missingSkills  Skills in JD but not in resume
     * @param extraSkills    Skills in resume but not in JD (not used in scoring)
     * @param jobDescription Full job description text
     * @param resumeText     Full resume text
     * @return ATS match score (10-95%)
     */
    public static double calculateScore(
            Set<String> matchedSkills,
            Set<String> missingSkills,
            Set<String> extraSkills, // Used for suggestions, not scoring
            String jobDescription,
            String resumeText) {

        // 1. Detect role intent and resume profile
        RoleIntent roleIntent = RoleIntentDetector.detect(jobDescription);
        ResumeProfile resumeProfile = ResumeProfileDetector.detect(resumeText);

        // 2. Calculate skill match score with vague JD protection
        double skillScore;
        int totalSignals = matchedSkills.size() + missingSkills.size();

        if (totalSignals < 3) {
            // Vague or non-technical JD (e.g., business/sales roles)
            // Give a baseline score to avoid division by zero
            skillScore = 0.4;
        } else {
            // Normal case: calculate match rate
            skillScore = (double) matchedSkills.size() / totalSignals;
        }

        // 3. Clamp skill score to realistic range (prevent extremes)
        skillScore = Math.max(0.25, Math.min(0.85, skillScore));

        // 4. Apply role×resume compatibility multiplier
        double compatibilityFactor = CompatibilityMatrix.compatibilityMultiplier(
                roleIntent, resumeProfile);

        // 5. Calculate final score
        double rawScore = skillScore * compatibilityFactor * 100;

        // 6. Apply human-safe boundaries (ATS never gives extremes)
        int finalScore = (int) Math.round(rawScore);
        finalScore = Math.max(10, Math.min(95, finalScore));

        return finalScore;
    }
}
