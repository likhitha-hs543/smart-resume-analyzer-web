package com.ats.analyzer.parser;

/**
 * Text normalization pipeline.
 * Converts raw text into clean, processable format.
 * 
 * Note: This is intentionally lossy:
 * - Number removal affects versions (Java 17 → java, HTML5 → html)
 * - Punctuation removal has edge cases (node.js → nodejs, c++ → c)
 * These are acceptable MVP tradeoffs documented in README.
 */
public class TextCleaner {

    /**
     * Clean and normalize text for keyword extraction
     * 
     * @param rawText Raw text from resume or JD
     * @return Normalized text
     */
    public static String clean(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return "";
        }

        String cleaned = rawText;

        // Convert to lowercase
        cleaned = cleaned.toLowerCase();

        // Remove numbers
        cleaned = cleaned.replaceAll("\\d+", " ");

        // Remove punctuation
        cleaned = cleaned.replaceAll("[^a-z\\s]", " ");

        // Replace multiple spaces with single space
        cleaned = cleaned.replaceAll("\\s+", " ");

        // Trim
        cleaned = cleaned.trim();

        return cleaned;
    }
}
