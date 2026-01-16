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

        // === DEBUG OUTPUT (remove in production) ===
        System.out.println("\n========== ATS SCORING DEBUG ==========");
        System.out.println("📋 JOB ANALYSIS:");
        System.out.println("  Role Intent: " + roleIntent);
        System.out.println("  Resume Profile: " + resumeProfile);
        System.out.println("\n📊 SKILLS BREAKDOWN:");
        System.out.println("  Matched (" + matchedSkills.size() + "): " + matchedSkills);
        System.out.println("  Missing (" + missingSkills.size() + "): " + missingSkills);

        // Identify core skills for debug
        Set<String> coreMatched = SkillClassifier.identifyCoreSkills(matchedSkills, roleIntent);
        Set<String> coreMissing = SkillClassifier.identifyCoreSkills(missingSkills, roleIntent);
        System.out.println("  Core Matched (" + coreMatched.size() + "): " + coreMatched);
        System.out.println("  Core Missing (" + coreMissing.size() + "): " + coreMissing);

        // 2. Calculate skill match score with importance weighting
        double skillScore = calculateWeightedSkillScore(
                matchedSkills, missingSkills, roleIntent);

        // 3. Clamp skill score to realistic range (tighter bounds to prevent inflation)
        skillScore = Math.max(0.20, Math.min(0.78, skillScore));

        // 4. Apply role×resume compatibility multiplier
        double compatibilityFactor = CompatibilityMatrix.compatibilityMultiplier(
                roleIntent, resumeProfile);

        // 5. Calculate final score
        double rawScore = skillScore * compatibilityFactor * 100;

        // 6. Apply human-safe boundaries (more realistic upper bound)
        int finalScore = (int) Math.round(rawScore);
        finalScore = Math.max(10, Math.min(92, finalScore));

        // === CONTINUE DEBUG ===
        System.out.println("\n🧮 SCORE CALCULATION:");
        System.out.println("  Skill Score: " + String.format("%.3f", skillScore));
        System.out.println("  Compatibility: " + String.format("%.3f", compatibilityFactor));
        System.out.println("  Raw Score: " + String.format("%.2f", rawScore));
        System.out.println("  Final Score: " + finalScore + "%");
        System.out.println("=======================================\n");

        return finalScore;
    }

    /**
     * Calculate skill score with importance weighting.
     * Core skills are weighted more heavily than nice-to-have skills.
     * Applies penalty if missing too many secondary skills.
     */
    private static double calculateWeightedSkillScore(
            Set<String> matchedSkills,
            Set<String> missingSkills,
            RoleIntent roleIntent) {

        int totalSignals = matchedSkills.size() + missingSkills.size();

        // Handle vague JDs (very few skills mentioned)
        if (totalSignals < 3) {
            return 0.35; // Reduced from 0.4 for more realistic baseline
        }

        // Classify skills as CORE vs SECONDARY
        Set<String> coreMatched = SkillClassifier.identifyCoreSkills(matchedSkills, roleIntent);
        Set<String> coreMissing = SkillClassifier.identifyCoreSkills(missingSkills, roleIntent);

        int totalCoreSkills = coreMatched.size() + coreMissing.size();

        // If there are core skills mentioned, prioritize them
        if (totalCoreSkills > 0) {
            // Core skill match rate
            double coreMatchRate = (double) coreMatched.size() / totalCoreSkills;

            // Overall skill match rate
            double overallMatchRate = (double) matchedSkills.size() / totalSignals;

            // Blended weighted score with adjusted weights
            // Core skills: 60% (reduced from 70% to prevent over-inflation)
            // Overall skills: 40% (increased from 30%)
            double weightedScore = (coreMatchRate * 0.6) + (overallMatchRate * 0.4);

            // Apply penalty if missing too many secondary skills
            // Even if core skills match 100%, missing 2x+ more skills should hurt
            if (missingSkills.size() > matchedSkills.size() * 2) {
                weightedScore *= 0.85; // 15% penalty for too many gaps
            }

            return weightedScore;
        } else {
            // No core skills detected - use simple match rate
            return (double) matchedSkills.size() / totalSignals;
        }
    }
}
