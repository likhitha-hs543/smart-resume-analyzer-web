package com.ats.analyzer.model;

/**
 * Classifies resume background for compatibility scoring.
 */
public enum ResumeProfile {
    /**
     * Technical background: strong programming/engineering signals
     */
    TECHNICAL,

    /**
     * Mixed background: some technical skills but not purely technical
     */
    MIXED,

    /**
     * Non-technical background: minimal technical signals
     */
    NON_TECH
}
