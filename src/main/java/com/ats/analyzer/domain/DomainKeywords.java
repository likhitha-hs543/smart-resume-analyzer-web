package com.ats.analyzer.domain;

import java.util.Map;
import java.util.Set;

/**
 * Maps domains to their characteristic keywords for classification.
 */
public class DomainKeywords {

    public static final Map<Domain, Set<String>> MAP = Map.of(
            Domain.ENGINEERING, Set.of(
                    "software", "developer", "engineer", "backend", "frontend",
                    "java", "python", "api", "system", "coding", "dsa", "algorithm",
                    "programming", "development", "web", "mobile", "application"),
            Domain.AI_DATA, Set.of(
                    "ai", "ml", "machine learning", "data", "model", "nlp",
                    "computer vision", "tensorflow", "pytorch", "deep learning",
                    "neural network", "data science", "analytics"),
            Domain.DEVOPS, Set.of(
                    "devops", "linux", "cloud", "docker",
                    "kubernetes", "aws", "azure", "automation", "cicd",
                    "jenkins", "terraform", "infrastructure", "deployment"),
            Domain.BUSINESS, Set.of(
                    "sales", "marketing", "growth", "business",
                    "client", "customer", "seo", "lead", "strategy",
                    "revenue", "partnership", "account", "management"));
}
