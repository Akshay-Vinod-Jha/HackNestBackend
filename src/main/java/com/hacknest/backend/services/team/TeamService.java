package com.hacknest.backend.services.team;

import com.hacknest.backend.dto.team.CreateTeamRequest;
import com.hacknest.backend.dto.team.TeamResponse;

public interface TeamService {
    TeamResponse createTeam(CreateTeamRequest request, String userId);
}
