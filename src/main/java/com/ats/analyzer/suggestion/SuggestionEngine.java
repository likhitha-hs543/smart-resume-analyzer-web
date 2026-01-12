package com.ats.analyzer.suggestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Rule-based suggestion generator.
 * Provides actionable feedback for resume improvement.
 */
public class SuggestionEngine {

    /**
     * Generate improvement suggestions based on match results
     * 
     * @param missingSkills Skills in JD but not in resume
     * @param extraSkills   Skills in resume but not in JD
     * @param score         ATS match score
     * @return List of actionable suggestions
     */
    public static List<String> generateSuggestions(Set<String> missingSkills, Set<String> extraSkills, double score) {
        List<String> suggestions = new ArrayList<>();

        // Suggest adding missing skills (sorted alphabetically, show first 3-5)
        if (!missingSkills.isEmpty()) {
            List<String> sortedMissing = missingSkills.stream()
                    .sorted()
                    .limit(5)
                    .collect(Collectors.toList());

            String skillList = String.join(", ", sortedMissing);
            suggestions.add("Consider adding experience with: " + skillList);
        }

        // Suggest highlighting relevance of extra skills
        if (!extraSkills.isEmpty() && !missingSkills.isEmpty()) {
            suggestions
                    .add("Align your additional skills (" + extraSkills.size() + " found) with the job requirements");
        }

        // Score-based feedback
        if (score < 50) {
            suggestions.add("Resume alignment needs significant improvement - focus on matching key job requirements");
        } else if (score >= 75) {
            suggestions.add("Strong alignment with job requirements");
        } else {
            suggestions.add("Moderate alignment - consider strengthening match with missing skills");
        }

        return suggestions;
    }
}
