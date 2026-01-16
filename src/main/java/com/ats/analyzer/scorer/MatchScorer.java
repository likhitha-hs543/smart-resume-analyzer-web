package com.ats.analyzer.scorer;

import com.ats.analyzer.logic.CompatibilityMatrix;
import com.ats.analyzer.logic.ResumeProfileDetector;
import com.ats.analyzer.logic.RoleIntentDetector;
import com.ats.analyzer.logic.SkillClassifier;
import com.ats.analyzer.model.ResumeProfile;
import com.ats.analyzer.model.RoleIntent;

import java.util.Set;

/**
 * Final ATS scoring engine with complete domain-aware logic.
 * Includes: skill weighting, role compatibility, design penalties, perfect core
 * bonus.
 */
public class MatchScorer {

    /**
     * Calculate complete ATS match score.
     * 
     * @param matchedSkills  Skills present in both resume and JD
     * @param missingSkills  Skills in JD but not in resume
     * @param extraSkills    Skills in resume but not in JD (not used in scoring)
     * @param jobDescription Full job description text
     * @param resumeText     Full resume text
     * @return ATS match score (10-90%)
     */
    public static double calculateScore(
            Set<String> matchedSkills,
            Set<String> missingSkills,
            Set<String> extraSkills,
            String jobDescription,
            String resumeText) {

        // 1. Detect role intent, resume profile, and design role
        RoleIntent roleIntent = RoleIntentDetector.detect(jobDescription);
        ResumeProfile resumeProfile = ResumeProfileDetector.detect(resumeText);
        boolean isDesignRole = RoleIntentDetector.isDesignRole(jobDescription);

        // === DEBUG OUTPUT ===
        System.out.println("\n========== ATS SCORING DEBUG ==========");
        System.out.println("📋 JOB ANALYSIS:");
        System.out.println("  Role Intent: " + roleIntent);
        System.out.println("  Is Design Role: " + isDesignRole);
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

        // 3. Clamp skill score to realistic range (tighter bounds)
        skillScore = Math.max(0.18, Math.min(0.76, skillScore));

        // 4. Apply role×resume compatibility multiplier
        double compatibilityFactor = CompatibilityMatrix.compatibilityMultiplier(
                roleIntent, resumeProfile);

        // 5. Apply design role penalty if applicable
        if (isDesignRole && resumeProfile == ResumeProfile.TECHNICAL) {
            compatibilityFactor *= 0.35; // Heavy penalty: dev skills ≠ design skills
            System.out.println("  ⚠️  Design Penalty Applied (0.35x)");
        }

        // 6. Calculate final score
        double rawScore = skillScore * compatibilityFactor * 100;

        // 7. Apply human-safe boundaries (realistic upper cap)
        int finalScore = (int) Math.round(rawScore);
        finalScore = Math.max(10, Math.min(90, finalScore));

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
     * Includes perfect core bonus for strong matches.
     */
    private static double calculateWeightedSkillScore(
            Set<String> matchedSkills,
            Set<String> missingSkills,
            RoleIntent roleIntent) {

        int totalSignals = matchedSkills.size() + missingSkills.size();

        // Handle vague JDs (very few skills mentioned)
        if (totalSignals < 3) {
            return 0.30; // Reduced baseline for vague JDs
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

            // Blended weighted score: core 60%, overall 40%
            double weightedScore = (coreMatchRate * 0.60) + (overallMatchRate * 0.40);

            // Penalty: if missing significantly more skills than matched
            if (missingSkills.size() > matchedSkills.size() * 2) {
                weightedScore *= 0.85; // 15% penalty
            }

            // Bonus: Perfect/near-perfect core match (95%+) AND at least 4 core skills
            // Prevents gaming with 1/1 core match
            if (coreMatchRate >= 0.95 && coreMatched.size() >= 4) {
                weightedScore = Math.min(0.76, weightedScore * 1.08); // 8% bonus, capped
                System.out.println("  ✨ Perfect Core Bonus Applied (+8%)");
            }

            return weightedScore;
        } else {
            // No core skills detected - use simple match rate
            return (double) matchedSkills.size() / totalSignals;
        }
    }
}
