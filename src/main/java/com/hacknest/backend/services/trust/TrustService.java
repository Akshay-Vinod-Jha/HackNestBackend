package com.hacknest.backend.services.trust;

import com.hacknest.backend.dto.trust.TrustScoreResponse;

public interface TrustService {
    int calculateReliabilityScore(String userId);
    int calculateContributionScore(String userId);
    TrustScoreResponse calculateTrustScore(String userId);
}
