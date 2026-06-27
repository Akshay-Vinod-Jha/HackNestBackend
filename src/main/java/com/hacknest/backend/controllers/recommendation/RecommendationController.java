package com.hacknest.backend.controllers.recommendation;

import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.dto.recommendation.HackathonRecommendation;
import com.hacknest.backend.dto.recommendation.TeamRecommendation;
import com.hacknest.backend.dto.recommendation.TeammateRecommendation;
import com.hacknest.backend.models.User;
import com.hacknest.backend.services.recommendation.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/teammates")
    public ResponseEntity<ApiResponse<List<TeammateRecommendation>>> getTeammateRecommendations(
            @RequestParam(required = false) String teamId,
            @AuthenticationPrincipal User user) {
        
        List<TeammateRecommendation> response;
        if (teamId == null || teamId.trim().isEmpty()) {
            // For now, return an empty list if no specific team context is provided on the dashboard
            response = java.util.Collections.emptyList();
        } else {
            response = recommendationService.getTeammateRecommendations(teamId, user.getId());
        }
        return ResponseEntity.ok(ApiResponse.success("Teammate recommendations generated successfully", response));
    }

    @GetMapping("/teams")
    public ResponseEntity<ApiResponse<List<TeamRecommendation>>> getTeamRecommendations(
            @AuthenticationPrincipal User user) {
        
        List<TeamRecommendation> response = recommendationService.getTeamRecommendations(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Team recommendations generated successfully", response));
    }

    @GetMapping("/hackathons")
    public ResponseEntity<ApiResponse<List<HackathonRecommendation>>> getHackathonRecommendations(
            @AuthenticationPrincipal User user) {
        
        List<HackathonRecommendation> response = recommendationService.getHackathonRecommendations(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Hackathon recommendations generated successfully", response));
    }
}
