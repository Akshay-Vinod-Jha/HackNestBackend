package com.hacknest.backend.dto.recommendation;

import com.hacknest.backend.dto.team.TeamSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamRecommendation {
    private TeamSummaryResponse team;
    private int matchScore;
    private List<RecommendationReason> reasons;
}
