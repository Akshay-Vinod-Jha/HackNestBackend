package com.hacknest.backend.services.team;

import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.dto.team.CreateTeamRequest;
import com.hacknest.backend.dto.team.RequiredRoleSummaryDto;
import com.hacknest.backend.dto.team.TeamResponse;
import com.hacknest.backend.dto.team.TeamSummaryResponse;
import com.hacknest.backend.enums.TeamStatus;
import com.hacknest.backend.models.team.RequiredRole;
import com.hacknest.backend.models.team.Team;
import com.hacknest.backend.repositories.HackathonRepository;
import com.hacknest.backend.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;

    @Override
    public TeamResponse createTeam(CreateTeamRequest request, String userId) {
        
        hackathonRepository.findById(request.getHackathonId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Hackathon ID"));

        int totalRequiredSlots = request.getRequiredRoles().stream()
                .mapToInt(role -> role.getSlots() != null ? role.getSlots() : 0)
                .sum();
        
        if (totalRequiredSlots + 1 > request.getMaxMembers()) {
            throw new IllegalArgumentException("Total required slots plus leader exceeds max members");
        }

        List<RequiredRole> mappedRoles = request.getRequiredRoles().stream()
                .map(dto -> RequiredRole.builder()
                        .roleName(dto.getRoleName())
                        .requiredSkills(dto.getRequiredSkills())
                        .slots(dto.getSlots())
                        .filledSlots(0)
                        .build())
                .collect(Collectors.toList());

        Team team = Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .hackathonId(request.getHackathonId())
                .leaderId(userId)
                .memberIds(Collections.singletonList(userId))
                .requiredRoles(mappedRoles)
                .requiredSkills(request.getRequiredSkills())
                .maxMembers(request.getMaxMembers())
                .currentMemberCount(1)
                .isOpen(true)
                .status(TeamStatus.RECRUITING)
                .teamCompletionPercentage(0)
                .build();

        team = teamRepository.save(team);

        return buildTeamResponse(team);
    }

    @Override
    public TeamResponse getTeamById(String teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        return buildTeamResponse(team);
    }

    @Override
    public PagedResponse<TeamSummaryResponse> getTeamsByHackathon(String hackathonId, int page, int size, String sortBy, String sortDirection) {
        hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Hackathon ID"));

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Team> teamPage = teamRepository.findByHackathonId(hackathonId, pageable);
        
        Page<TeamSummaryResponse> summaryPage = teamPage.map(team -> {
            List<RequiredRoleSummaryDto> roles = team.getRequiredRoles().stream()
                .map(r -> RequiredRoleSummaryDto.builder()
                        .roleName(r.getRoleName())
                        .slots(r.getSlots())
                        .filledSlots(r.getFilledSlots())
                        .build())
                .collect(Collectors.toList());
                
            return TeamSummaryResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .leaderId(team.getLeaderId())
                .requiredRoles(roles)
                .maxMembers(team.getMaxMembers())
                .currentMemberCount(team.getCurrentMemberCount())
                .isOpen(team.getIsOpen())
                .status(team.getStatus())
                .build();
        });
        
        return PagedResponse.of(summaryPage);
    }

    private TeamResponse buildTeamResponse(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .hackathonId(team.getHackathonId())
                .leaderId(team.getLeaderId())
                .memberIds(team.getMemberIds())
                .requiredRoles(team.getRequiredRoles())
                .requiredSkills(team.getRequiredSkills())
                .maxMembers(team.getMaxMembers())
                .currentMemberCount(team.getCurrentMemberCount())
                .isOpen(team.getIsOpen())
                .status(team.getStatus())
                .teamCompletionPercentage(team.getTeamCompletionPercentage())
                .createdAt(team.getCreatedAt())
                .updatedAt(team.getUpdatedAt())
                .build();
    }
}
