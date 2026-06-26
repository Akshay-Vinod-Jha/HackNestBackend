package com.hacknest.backend.services.trust;

public interface TrustService {
    int calculateReliabilityScore(String userId);
    int calculateContributionScore(String userId);
}
