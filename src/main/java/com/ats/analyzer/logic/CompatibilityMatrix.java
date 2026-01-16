package com.ats.analyzer.logic;

import com.ats.analyzer.model.RoleIntent;
import com.ats.analyzer.model.ResumeProfile;

/**
 * Role×Resume compatibility matrix.
 * Final tuned values to prevent score inflation.
 */
public class CompatibilityMatrix {

    /**
     * Calculate compatibility multiplier based on role-resume alignment.
     * 
     * @param role    Job role intent
     * @param profile Resume background profile
     * @return Compatibility multiplier (0.26 to 0.92)
     */
    public static double compatibilityMultiplier(
            RoleIntent role,
            ResumeProfile profile) {

        return switch (role) {
            case TECH_CORE -> switch (profile) {
                case TECHNICAL -> 0.88; // Final reduction
                case MIXED -> 0.63; // Final reduction
                case NON_TECH -> 0.26; // Final reduction
            };

            case TECH_ADJACENT -> switch (profile) {
                case TECHNICAL -> 0.48; // Final reduction
                case MIXED -> 0.70; // Final reduction
                case NON_TECH -> 0.46; // Final reduction
            };

            case NON_TECH -> switch (profile) {
                case TECHNICAL -> 0.35; // Final reduction
                case MIXED -> 0.65; // Final reduction
                case NON_TECH -> 0.92; // Final reduction (no perfect 1.0)
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
