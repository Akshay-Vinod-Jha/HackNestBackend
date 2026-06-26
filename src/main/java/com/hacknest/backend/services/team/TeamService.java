package com.hacknest.backend.services.team;

import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.dto.team.CreateTeamRequest;
import com.hacknest.backend.dto.team.TeamResponse;
import com.hacknest.backend.dto.team.TeamSummaryResponse;

public interface TeamService {
    TeamResponse createTeam(CreateTeamRequest request, String userId);
    TeamResponse getTeamById(String teamId);
    PagedResponse<TeamSummaryResponse> getTeamsByHackathon(String hackathonId, int page, int size, String sortBy, String sortDirection);
    PagedResponse<TeamSummaryResponse> searchTeams(String hackathonId, String status, Boolean isOpen, String requiredSkill, String requiredRole, Integer teamSizeMin, Integer teamSizeMax, Integer teamCompletionMin, int page, int size, String sortBy, String sortDirection);
}
