package com.ats.analyzer.logic;

import com.ats.analyzer.model.RoleIntent;

/**
 * Defines skill weighting policies based on role intent.
 * Different role types have different expectations for technical skill matches.
 */
public class SkillWeightingPolicy {

    /**
     * Weight for core skill matches (how much matched skills contribute to score).
     * 
     * @param intent Role intent
     * @return Weight multiplier (0.0 to 1.0)
     */
    public static double coreSkillWeight(RoleIntent intent) {
        return switch (intent) {
            case TECH_CORE -> 0.7; // Technical roles: skills are critical
            case TECH_ADJACENT -> 0.4; // Business roles: skills matter less
            case NON_TECH -> 0.2; // Non-tech: skills barely matter
        };
    }

    /**
     * Penalty for missing skills (how much missing skills reduce score).
     * 
     * @param intent Role intent
     * @return Penalty multiplier (0.0 to 1.0)
     */
    public static double toolPenalty(RoleIntent intent) {
        return switch (intent) {
            case TECH_CORE -> 0.3; // Missing tools hurt significantly
            case TECH_ADJACENT -> 0.15; // Missing tools hurt moderately
            case NON_TECH -> 0.05; // Missing tools barely matter
        };
    }

    /**
     * Minimum score floor (prevents unrealistic 0% scores).
     * 
     * @param intent Role intent
     * @return Minimum score percentage
     */
    public static int minimumScoreFloor(RoleIntent intent) {
        return switch (intent) {
            case TECH_CORE -> 15; // Even bad matches get 15%
            case TECH_ADJACENT -> 20; // Business roles more forgiving
            case NON_TECH -> 10; // Lowest floor
        };
    }
}
