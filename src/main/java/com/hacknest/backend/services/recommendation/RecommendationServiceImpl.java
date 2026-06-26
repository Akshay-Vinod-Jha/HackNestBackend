package com.hacknest.backend.services.recommendation;

import com.hacknest.backend.dto.recommendation.RecommendationReason;
import com.hacknest.backend.dto.recommendation.TeammateRecommendation;
import com.hacknest.backend.models.User;
import com.hacknest.backend.models.hackathon.Hackathon;
import com.hacknest.backend.models.profile.Skill;
import com.hacknest.backend.models.team.RequiredRole;
import com.hacknest.backend.models.team.Team;
import com.hacknest.backend.repositories.HackathonRepository;
import com.hacknest.backend.repositories.TeamRepository;
import com.hacknest.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;

    @Override
    public List<TeammateRecommendation> getTeammateRecommendations(String teamId, String leaderId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        if (!team.getLeaderId().equals(leaderId)) {
            throw new IllegalArgumentException("Only the team leader can view teammate recommendations");
        }

        Hackathon hackathon = hackathonRepository.findById(team.getHackathonId())
                .orElse(null);

        User leader = userRepository.findById(leaderId).orElse(null);
        String leaderCollege = (leader != null && leader.getProfile() != null) ? leader.getProfile().getCollege() : null;

        List<String> teamSkills = new ArrayList<>();
        if (team.getRequiredRoles() != null) {
            for (RequiredRole role : team.getRequiredRoles()) {
                if (role.getRequiredSkills() != null) {
                    teamSkills.addAll(role.getRequiredSkills().stream().map(String::toLowerCase).collect(Collectors.toList()));
                }
            }
        }

        List<String> hackathonDomains = new ArrayList<>();
        if (hackathon != null && hackathon.getDomains() != null) {
            hackathonDomains = hackathon.getDomains().stream().map(String::toLowerCase).collect(Collectors.toList());
        }

        List<User> allUsers = userRepository.findAll();
        List<TeammateRecommendation> recommendations = new ArrayList<>();

        for (User user : allUsers) {
            if (user.getId().equals(leaderId) || team.getMemberIds().contains(user.getId())) {
                continue; // Skip existing members and the leader themselves
            }

            int score = 0;
            List<RecommendationReason> reasons = new ArrayList<>();

            if (user.getProfile() != null) {
                // 1. Skill Match (+30)
                if (user.getProfile().getSkills() != null && !teamSkills.isEmpty()) {
                    boolean skillMatch = user.getProfile().getSkills().stream()
                            .map(Skill::getName)
                            .map(String::toLowerCase)
                            .anyMatch(teamSkills::contains);
                    if (skillMatch) {
                        score += 30;
                        reasons.add(new RecommendationReason("Matching Skill found", 30));
                    }
                }

                // 2. College Match (+10)
                String userCollege = user.getProfile().getCollege();
                if (StringUtils.hasText(userCollege) && StringUtils.hasText(leaderCollege) && userCollege.equalsIgnoreCase(leaderCollege)) {
                    score += 10;
                    reasons.add(new RecommendationReason("Matching College with leader", 10));
                }

                // 3. Domain Match (+20)
                if (user.getProfile().getInterests() != null && !hackathonDomains.isEmpty()) {
                    boolean domainMatch = user.getProfile().getInterests().stream()
                            .map(String::toLowerCase)
                            .anyMatch(hackathonDomains::contains);
                    if (domainMatch) {
                        score += 20;
                        reasons.add(new RecommendationReason("Matching Domain/Interest", 20));
                    }
                }

                // 4. Profile Completion > 80 (+20)
                if (user.getProfile().getProfileCompletionPercentage() != null && user.getProfile().getProfileCompletionPercentage() > 80) {
                    score += 20;
                    reasons.add(new RecommendationReason("Profile Completion > 80%", 20));
                }
                
                // 5. Trust Score (+20) - Future
            }

            if (score > 0) {
                recommendations.add(TeammateRecommendation.builder()
                        .user(user)
                        .matchScore(score)
                        .reasons(reasons)
                        .build());
            }
        }

        // Sort descending by match score
        recommendations.sort((a, b) -> Integer.compare(b.getMatchScore(), a.getMatchScore()));

        // Return top 20
        return recommendations.stream().limit(20).collect(Collectors.toList());
    }
}
