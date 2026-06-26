package com.hacknest.backend.services.recommendation;

import com.hacknest.backend.dto.recommendation.TeammateRecommendation;
import java.util.List;

public interface RecommendationService {
    List<TeammateRecommendation> getTeammateRecommendations(String teamId, String leaderId);
}
