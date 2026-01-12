package com.ats.analyzer.suggestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generates actionable, ranked suggestions for resume improvement.
 * Philosophy: Max 3 suggestions - prioritized and non-repetitive.
 */
public class SuggestionEngine {

    public static List<String> generateSuggestions(
            Set<String> missingSkills,
            Set<String> extraSkills,
            double score) {

        List<String> suggestions = new ArrayList<>();

        // 1️⃣ Primary blocker - top missing skill (ONLY ONE)
        if (!missingSkills.isEmpty()) {
            // Sort alphabetically for deterministic output
            List<String> sortedMissing = missingSkills.stream()
                    .sorted()
                    .collect(Collectors.toList());

            String topMissing = sortedMissing.get(0);
            suggestions.add(
                    "Add experience with \"" + topMissing +
                            "\" by mentioning it in a project or internship.");
        }

        // 2️⃣ Secondary insight - extra skills strategy
        if (extraSkills.size() > 5) {
            suggestions.add(
                    "You listed " + extraSkills.size() +
                            " skills not in the job description. Consider prioritizing only the most relevant ones.");
        }

        // 3️⃣ Contextual tip based on score
        if (score < 50) {
            suggestions.add(
                    "Focus on matching core job requirements before adding additional technologies.");
        } else if (score < 80) {
            suggestions.add(
                    "Add missing skills naturally within experience or project descriptions instead of listing them separately.");
        } else {
            suggestions.add(
                    "Strong alignment! Consider adding specific examples or metrics for matched skills.");
        }

        return suggestions;
    }
}
