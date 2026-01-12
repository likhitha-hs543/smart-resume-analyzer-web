package com.ats.analyzer.matcher;

import java.util.Collections;
import java.util.Set;

/**
 * Data model for skill matching results.
 * Uses immutable sets for defensive programming.
 */
public class MatchResult {

    private final Set<String> matchedSkills;
    private final Set<String> missingSkills;
    private final Set<String> extraSkills;

    public MatchResult(Set<String> matchedSkills, Set<String> missingSkills, Set<String> extraSkills) {
        this.matchedSkills = Collections.unmodifiableSet(matchedSkills);
        this.missingSkills = Collections.unmodifiableSet(missingSkills);
        this.extraSkills = Collections.unmodifiableSet(extraSkills);
    }

    public Set<String> getMatchedSkills() {
        return matchedSkills;
    }

    public Set<String> getMissingSkills() {
        return missingSkills;
    }

    public Set<String> getExtraSkills() {
        return extraSkills;
    }
}
