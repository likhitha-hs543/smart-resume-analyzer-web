package com.ats.analyzer.domain;

import java.util.Set;

/**
 * Classifies documents (resume or JD) into domains based on keyword presence.
 */
public class DomainClassifier {

    /**
     * Detects the primary domain of a document based on keyword hits.
     * 
     * @param tokens Set of keywords from the document
     * @return The domain with the most keyword matches
     */
    public static Domain detect(Set<String> tokens) {
        Domain bestMatch = Domain.GENERAL;
        int maxHits = 0;

        for (var entry : DomainKeywords.MAP.entrySet()) {
            int hits = 0;
            for (String keyword : entry.getValue()) {
                if (tokens.contains(keyword.toLowerCase())) {
                    hits++;
                }
            }
            if (hits > maxHits) {
                maxHits = hits;
                bestMatch = entry.getKey();
            }
        }

        return bestMatch;
    }
}
