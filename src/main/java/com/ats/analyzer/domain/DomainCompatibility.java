package com.ats.analyzer.domain;

/**
 * Determines compatibility between resume and job domains.
 * Fixes 0%/100% absurdity by considering transferability.
 */
public class DomainCompatibility {

    /**
     * Calculates domain compatibility factor.
     * 
     * @param resume Resume domain
     * @param jd     Job description domain
     * @return Compatibility multiplier (0.25 to 1.0)
     */
    public static double factor(Domain resume, Domain jd) {

        // Perfect match
        if (resume == jd) {
            return 1.0;
        }

        // Transferable tech domains
        if ((resume == Domain.ENGINEERING && jd == Domain.AI_DATA) ||
                (resume == Domain.AI_DATA && jd == Domain.ENGINEERING) ||
                (resume == Domain.ENGINEERING && jd == Domain.DEVOPS) ||
                (resume == Domain.DEVOPS && jd == Domain.ENGINEERING) ||
                (resume == Domain.AI_DATA && jd == Domain.DEVOPS) ||
                (resume == Domain.DEVOPS && jd == Domain.AI_DATA)) {
            return 0.6;
        }

        // Weakly related (prevents 0% scores)
        return 0.25;
    }
}
