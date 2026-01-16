package com.ats.analyzer.logic;

import com.ats.analyzer.model.RoleIntent;
import com.ats.analyzer.model.ResumeProfile;

/**
 * Role×Resume compatibility matrix.
 * Determines how well a resume background fits a job role type.
 * This is THE FIX for unrealistic cross-domain scores.
 */
public class CompatibilityMatrix {

    /**
     * Calculate compatibility multiplier based on role-resume alignment.
     * 
     * @param role    Job role intent
     * @param profile Resume background profile
     * @return Compatibility multiplier (0.3 to 1.0)
     */
    public static double compatibilityMultiplier(
            RoleIntent role,
            ResumeProfile profile) {

        return switch (role) {
            case TECH_CORE -> switch (profile) {
                case TECHNICAL -> 0.95; // Reduced from 1.0 to prevent inflation
                case MIXED -> 0.7; // Decent fit
                case NON_TECH -> 0.3; // Weak fit
            };

            case TECH_ADJACENT -> switch (profile) {
                case TECHNICAL -> 0.55; // Reduced from 0.6
                case MIXED -> 0.75; // Reduced from 0.8
                case NON_TECH -> 0.5; // Acceptable
            };

            case NON_TECH -> switch (profile) {
                case TECHNICAL -> 0.4; // Career switch, possible but weaker
                case MIXED -> 0.7; // Decent transition
                case NON_TECH -> 1.0; // Perfect match
            };
        };
    }

    /**
     * Get minimum score floor based on role type.
     * Prevents unrealistic 0% scores.
     * 
     * @param role Job role intent
     * @return Minimum score percentage
     */
    public static int minimumFloor(RoleIntent role) {
        return switch (role) {
            case TECH_CORE -> 20;
            case TECH_ADJACENT -> 15;
            case NON_TECH -> 10;
        };
    }
}
