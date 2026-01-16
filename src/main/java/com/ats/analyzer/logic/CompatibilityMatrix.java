package com.ats.analyzer.logic;

import com.ats.analyzer.model.RoleIntent;
import com.ats.analyzer.model.ResumeProfile;

/**
 * Role×Resume compatibility matrix.
 * Determines how well a resume background fits a job role type.
 * Values further reduced to prevent score inflation.
 */
public class CompatibilityMatrix {

    /**
     * Calculate compatibility multiplier based on role-resume alignment.
     * 
     * @param role    Job role intent
     * @param profile Resume background profile
     * @return Compatibility multiplier (0.28 to 0.95)
     */
    public static double compatibilityMultiplier(
            RoleIntent role,
            ResumeProfile profile) {

        return switch (role) {
            case TECH_CORE -> switch (profile) {
                case TECHNICAL -> 0.90; // Further reduced from 0.95
                case MIXED -> 0.65; // Reduced from 0.70
                case NON_TECH -> 0.28; // Reduced from 0.30
            };

            case TECH_ADJACENT -> switch (profile) {
                case TECHNICAL -> 0.50; // Further reduced from 0.55
                case MIXED -> 0.72; // Reduced from 0.75
                case NON_TECH -> 0.48; // Reduced from 0.50
            };

            case NON_TECH -> switch (profile) {
                case TECHNICAL -> 0.38; // Reduced from 0.40
                case MIXED -> 0.68; // Reduced from 0.70
                case NON_TECH -> 0.95; // Reduced from 1.00
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
