package com.ats.analyzer.logic;

import com.ats.analyzer.model.RoleIntent;
import com.ats.analyzer.model.ResumeProfile;

/**
 * Role×Resume compatibility matrix.
 * Adjusted values after emergency testing.
 */
public class CompatibilityMatrix {

    /**
     * Calculate compatibility multiplier based on role-resume alignment.
     * 
     * @param role    Job role intent
     * @param profile Resume background profile
     * @return Compatibility multiplier
     */
    public static double compatibilityMultiplier(
            RoleIntent role,
            ResumeProfile profile) {

        return switch (role) {
            case TECH_CORE -> switch (profile) {
                case TECHNICAL -> 0.95; // Increased back from 0.88
                case MIXED -> 0.70; // Increased from 0.63
                case NON_TECH -> 0.30; // Increased from 0.26
            };

            case TECH_ADJACENT -> switch (profile) {
                case TECHNICAL -> 0.52; // Increased from 0.48
                case MIXED -> 0.75; // Increased from 0.70
                case NON_TECH -> 0.50; // Increased from 0.46
            };

            case NON_TECH -> switch (profile) {
                case TECHNICAL -> 0.38; // Increased from 0.35
                case MIXED -> 0.68; // Increased from 0.65
                case NON_TECH -> 0.95; // Increased from 0.92
            };
        };
    }

    /**
     * Get minimum score floor based on role type.
     */
    public static int minimumFloor(RoleIntent role) {
        return switch (role) {
            case TECH_CORE -> 20;
            case TECH_ADJACENT -> 15;
            case NON_TECH -> 10;
        };
    }
}
