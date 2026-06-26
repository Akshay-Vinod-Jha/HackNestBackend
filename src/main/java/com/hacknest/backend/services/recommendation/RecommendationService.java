package com.hacknest.backend.services.recommendation;

import com.hacknest.backend.dto.recommendation.TeamRecommendation;
import com.hacknest.backend.dto.recommendation.TeammateRecommendation;
import java.util.List;

public interface RecommendationService {
    List<TeammateRecommendation> getTeammateRecommendations(String teamId, String leaderId);
    List<TeamRecommendation> getTeamRecommendations(String userId);
}
