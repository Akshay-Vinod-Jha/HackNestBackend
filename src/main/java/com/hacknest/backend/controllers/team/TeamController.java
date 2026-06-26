package com.hacknest.backend.controllers.team;

import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.dto.team.CreateTeamRequest;
import com.hacknest.backend.dto.team.TeamResponse;
import com.hacknest.backend.models.User;
import com.hacknest.backend.services.team.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateTeamRequest request) {
        
        TeamResponse response = teamService.createTeam(request, user.getId());
        return new ResponseEntity<>(ApiResponse.success("Team created successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeamById(@PathVariable String id) {
        TeamResponse response = teamService.getTeamById(id);
        return ResponseEntity.ok(ApiResponse.success("Team fetched successfully", response));
    }
}
