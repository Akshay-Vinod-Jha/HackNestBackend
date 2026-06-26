package com.hacknest.backend.services.team;

import com.hacknest.backend.dto.team.TeamAnalysisResponse;

public interface TeamAnalysisService {
    TeamAnalysisResponse analyzeTeam(String teamId);
}
