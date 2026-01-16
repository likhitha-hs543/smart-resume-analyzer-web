package com.ats.analyzer.scorer;

import com.ats.analyzer.domain.Domain;
import com.ats.analyzer.domain.DomainClassifier;
import com.ats.analyzer.domain.DomainCompatibility;

import java.util.Set;

/**
 * Domain-aware ATS scoring engine.
 * Considers job-resume domain compatibility and skill transferability.
 * Produces human-safe scores (10-95% range, no extremes).
 */
public class MatchScorer {

    /**
     * Calculate domain-aware ATS match score.
     * 
     * @param matchedSkills Skills present in both resume and JD
     * @param jdSkills      All skills from job description
     * @param resumeSkills  All skills from resume
     * @return ATS match score (10-95%)
     */
    public static double calculateScore(
            Set<String> matchedSkills,
            Set<String> jdSkills,
            Set<String> resumeSkills) {

        if (jdSkills == null || jdSkills.isEmpty()) {
            return 0.0;
        }

        // 1. Detect domains
        Domain resumeDomain = DomainClassifier.detect(resumeSkills);
        Domain jdDomain = DomainClassifier.detect(jdSkills);

        // 2. Calculate base skill match percentage
        double skillMatchRate = (double) matchedSkills.size() / jdSkills.size();

        // 3. Apply domain compatibility factor
        double domainFactor = DomainCompatibility.factor(resumeDomain, jdDomain);

        // 4. Calculate final score
        double rawScore = skillMatchRate * domainFactor * 100;

        // 5. Apply human-safe boundaries (ATS never gives extremes)
        double finalScore = Math.max(rawScore, 10);
        finalScore = Math.min(finalScore, 95);

        return finalScore;
    }
}
