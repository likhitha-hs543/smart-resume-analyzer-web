package com.ats.web.dto;

import java.util.List;
import java.util.Set;

public class AnalysisResult {

    private final double score;
    private final Set<String> matchedSkills;
    private final Set<String> missingSkills;
    private final Set<String> extraSkills;
    private final List<String> suggestions;

    public AnalysisResult(
            double score,
            Set<String> matchedSkills,
            Set<String> missingSkills,
            Set<String> extraSkills,
            List<String> suggestions) {
        this.score = score;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
        this.extraSkills = extraSkills;
        this.suggestions = suggestions;
    }

    public double getScore() {
        return score;
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

    public List<String> getSuggestions() {
        return suggestions;
    }
}
