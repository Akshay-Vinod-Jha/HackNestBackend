package com.hacknest.backend.services.team;

import com.hacknest.backend.dto.team.TeamAnalysisResponse;
import com.hacknest.backend.models.User;
import com.hacknest.backend.models.profile.Skill;
import com.hacknest.backend.models.team.RequiredRole;
import com.hacknest.backend.models.team.Team;
import com.hacknest.backend.repositories.TeamRepository;
import com.hacknest.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamAnalysisServiceImpl implements TeamAnalysisService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    public TeamAnalysisResponse analyzeTeam(String teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        List<String> missingRoles = new ArrayList<>();
        Set<String> allRequiredSkills = new HashSet<>();
        
        int totalSlots = 0;
        int filledSlots = 0;

        if (team.getRequiredRoles() != null) {
            for (RequiredRole role : team.getRequiredRoles()) {
                totalSlots += role.getSlots();
                filledSlots += role.getFilledSlots();
                
                if (role.getSlots() > role.getFilledSlots()) {
                    missingRoles.add(role.getRoleName());
                }

                if (role.getRequiredSkills() != null) {
                    allRequiredSkills.addAll(role.getRequiredSkills().stream()
                            .map(String::toLowerCase)
                            .collect(Collectors.toList()));
                }
            }
        }

        List<String> memberIds = new ArrayList<>();
        memberIds.add(team.getLeaderId());
        if (team.getMemberIds() != null) {
            memberIds.addAll(team.getMemberIds());
        }

        Set<String> currentTeamSkills = new HashSet<>();
        Iterable<User> membersIterable = userRepository.findAllById(memberIds);
        List<User> members = new ArrayList<>();
        membersIterable.forEach(members::add);

        for (User member : members) {
            if (member.getProfile() != null && member.getProfile().getSkills() != null) {
                currentTeamSkills.addAll(member.getProfile().getSkills().stream()
                        .map(Skill::getName)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList()));
            }
        }

        List<String> missingSkills = new ArrayList<>();
        int coveredSkillsCount = 0;

        for (String reqSkill : allRequiredSkills) {
            if (currentTeamSkills.contains(reqSkill)) {
                coveredSkillsCount++;
            } else {
                missingSkills.add(reqSkill);
            }
        }

        double roleScore = totalSlots == 0 ? 50.0 : ((double) filledSlots / totalSlots) * 50.0;
        double skillScore = allRequiredSkills.isEmpty() ? 50.0 : ((double) coveredSkillsCount / allRequiredSkills.size()) * 50.0;

        int alignmentScore = (int) Math.round(roleScore + skillScore);

        return TeamAnalysisResponse.builder()
                .alignmentScore(alignmentScore)
                .missingRoles(missingRoles)
                .missingSkills(missingSkills)
                .build();
    }
}
