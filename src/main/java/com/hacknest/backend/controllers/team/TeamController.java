package com.hacknest.backend.controllers.team;

import com.hacknest.backend.dto.application.ApplicationResponse;
import com.hacknest.backend.dto.application.ApplicationSummaryResponse;
import com.hacknest.backend.dto.application.ApplyRequest;
import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.dto.invitation.InvitationResponse;
import com.hacknest.backend.dto.invitation.SendInvitationRequest;
import com.hacknest.backend.dto.team.CreateTeamRequest;
import com.hacknest.backend.dto.team.TeamResponse;
import com.hacknest.backend.dto.team.TeamSummaryResponse;
import com.hacknest.backend.enums.ApplicationStatus;
import com.hacknest.backend.models.User;
import com.hacknest.backend.services.application.ApplicationService;
import com.hacknest.backend.services.invitation.InvitationService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final ApplicationService applicationService;
    private final InvitationService invitationService;

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

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<TeamSummaryResponse>>> searchTeams(
            @RequestParam(required = false) String hackathonId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean isOpen,
            @RequestParam(required = false) String requiredSkill,
            @RequestParam(required = false) String requiredRole,
            @RequestParam(required = false) Integer teamSizeMin,
            @RequestParam(required = false) Integer teamSizeMax,
            @RequestParam(required = false) Integer teamCompletionMin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        PagedResponse<TeamSummaryResponse> response = teamService.searchTeams(
            hackathonId, status, isOpen, requiredSkill, requiredRole, teamSizeMin, teamSizeMax, teamCompletionMin, page, size, sortBy, sortDirection);
            
        return ResponseEntity.ok(ApiResponse.success("Teams filtered successfully", response));
    }

    @PostMapping("/{teamId}/apply")
    public ResponseEntity<ApiResponse<ApplicationResponse>> applyToTeam(
            @PathVariable String teamId,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ApplyRequest request) {
        
        ApplicationResponse response = applicationService.applyToTeam(teamId, user.getId(), request);
        return new ResponseEntity<>(ApiResponse.success("Application submitted successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/{teamId}/applications")
    public ResponseEntity<ApiResponse<PagedResponse<ApplicationSummaryResponse>>> getTeamApplications(
            @PathVariable String teamId,
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        PagedResponse<ApplicationSummaryResponse> response = applicationService.getTeamApplications(
                teamId, user.getId(), status, page, size, sortBy, sortDirection);
                
        return ResponseEntity.ok(ApiResponse.success("Applications fetched successfully", response));
    }

    @PostMapping("/{teamId}/invite")
    public ResponseEntity<ApiResponse<InvitationResponse>> inviteToTeam(
            @PathVariable String teamId,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SendInvitationRequest request) {
        
        InvitationResponse response = invitationService.sendInvitation(teamId, user.getId(), request);
        return new ResponseEntity<>(ApiResponse.success("Invitation sent successfully", response), HttpStatus.CREATED);
    }
}
