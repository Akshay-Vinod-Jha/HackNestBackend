package com.hacknest.backend.services.team;

import com.hacknest.backend.dto.team.CreateTeamRequest;
import com.hacknest.backend.dto.team.TeamResponse;
import com.hacknest.backend.enums.TeamStatus;
import com.hacknest.backend.models.team.RequiredRole;
import com.hacknest.backend.models.team.Team;
import com.hacknest.backend.repositories.HackathonRepository;
import com.hacknest.backend.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
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
