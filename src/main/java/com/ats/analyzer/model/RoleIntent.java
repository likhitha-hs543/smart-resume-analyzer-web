package com.ats.analyzer.model;

/**
 * Classifies job roles into intent categories for realistic ATS scoring.
 * Real ATS systems differentiate between technical and non-technical roles.
 */
public enum RoleIntent {
    /**
     * Core technical roles: SDE, AI Engineer, DevOps, Data Scientist
     */
    TECH_CORE,

    /**
     * Technical-adjacent roles: Marketing, Business Analyst, Growth, Operations
     */
    TECH_ADJACENT,

    /**
     * Non-technical roles: Sales, HR, Customer Success
     */
    NON_TECH
}
