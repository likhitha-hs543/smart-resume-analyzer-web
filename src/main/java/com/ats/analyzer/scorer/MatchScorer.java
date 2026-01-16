package com.ats.analyzer.scorer;

import com.ats.analyzer.logic.CompatibilityMatrix;
import com.ats.analyzer.logic.ResumeProfileDetector;
import com.ats.analyzer.logic.RoleIntentDetector;
import com.ats.analyzer.logic.SkillClassifier;
import com.ats.analyzer.model.ResumeProfile;
import com.ats.analyzer.model.RoleIntent;

import java.util.Set;

/**
 * Final ATS scoring engine with domain-aware logic and skill importance
 * weighting.
 * Produces realistic scores (10-95%) that consider:
 * 1. Skill match (with core skill priority)
 * 2. Role compatibility
 * 
 * Design Philosophy:
 * - Deterministic (no ML)
 * - Explainable (rule-based)
 * - Realistic (no 0% or 100% extremes)
 * - Fair (core skills weighted more than nice-to-have)
 */
public class MatchScorer {

    /**
     * Calculate complete ATS match score with role compatibility and skill
     * weighting.
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

        // 2. Calculate skill match score with importance weighting
        double skillScore = calculateWeightedSkillScore(
                matchedSkills, missingSkills, roleIntent);

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

    /**
     * Calculate skill score with importance weighting.
     * Core skills are weighted more heavily than nice-to-have skills.
     */
    private static double calculateWeightedSkillScore(
            Set<String> matchedSkills,
            Set<String> missingSkills,
            RoleIntent roleIntent) {

        int totalSignals = matchedSkills.size() + missingSkills.size();

        // Handle vague JDs (very few skills mentioned)
        if (totalSignals < 3) {
            return 0.4; // Baseline score for vague JDs
        }

        // Classify skills as CORE vs SECONDARY
        Set<String> coreMatched = SkillClassifier.identifyCoreSkills(matchedSkills, roleIntent);
        Set<String> coreMissing = SkillClassifier.identifyCoreSkills(missingSkills, roleIntent);

        int totalCoreSkills = coreMatched.size() + coreMissing.size();

        // If there are core skills mentioned, prioritize them
        if (totalCoreSkills > 0) {
            // Core skill match rate (weighted 70%)
            double coreMatchRate = (double) coreMatched.size() / totalCoreSkills;

            // Overall skill match rate (weighted 30%)
            double overallMatchRate = (double) matchedSkills.size() / totalSignals;

            // Weighted combination: core skills matter more
            return (coreMatchRate * 0.7) + (overallMatchRate * 0.3);
        } else {
            // No core skills detected - use simple match rate
            return (double) matchedSkills.size() / totalSignals;
        }
    }
}
