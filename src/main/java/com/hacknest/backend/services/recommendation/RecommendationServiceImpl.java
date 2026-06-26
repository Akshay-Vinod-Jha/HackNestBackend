package com.hacknest.backend.services.recommendation;

import com.hacknest.backend.dto.recommendation.HackathonRecommendation;
import com.hacknest.backend.dto.recommendation.RecommendationReason;
import com.hacknest.backend.dto.recommendation.TeamRecommendation;
import com.hacknest.backend.dto.recommendation.TeammateRecommendation;
import com.hacknest.backend.dto.team.TeamSummaryResponse;
import com.hacknest.backend.enums.TeamStatus;
import com.hacknest.backend.enums.HackathonStatus;
import com.hacknest.backend.models.User;
import com.hacknest.backend.models.hackathon.Hackathon;
import com.hacknest.backend.models.profile.Skill;
import com.hacknest.backend.models.team.RequiredRole;
import com.hacknest.backend.models.team.Team;
import com.hacknest.backend.repositories.HackathonRepository;
import com.hacknest.backend.repositories.TeamRepository;
import com.hacknest.backend.repositories.UserRepository;
import com.hacknest.backend.services.trust.TrustService;
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
    private final TrustService trustService;

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
                
                // 5. Trust Score (+20)
                int trustScore = trustService.calculateTrustScore(user.getId()).getTrustScore();
                if (trustScore > 0) {
                    int trustImpact = (int) Math.round((trustScore / 100.0) * 20.0);
                    if (trustImpact > 0) {
                        score += trustImpact;
                        reasons.add(new RecommendationReason("High Trust Score (" + trustScore + "/100)", trustImpact));
                    }
                }
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

    @Override
    public List<TeamRecommendation> getTeamRecommendations(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<String> userSkills = new ArrayList<>();
        List<String> userInterests = new ArrayList<>();
        if (user.getProfile() != null) {
            if (user.getProfile().getSkills() != null) {
                userSkills = user.getProfile().getSkills().stream()
                        .map(Skill::getName)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
            }
            if (user.getProfile().getInterests() != null) {
                userInterests = user.getProfile().getInterests().stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
            }
        }

        List<Team> allTeams = teamRepository.findAll();
        List<TeamRecommendation> recommendations = new ArrayList<>();

        for (Team team : allTeams) {
            // Skip teams where the user is already a member or the leader, or if the team is full
            if (team.getLeaderId().equals(userId) || 
                (team.getMemberIds() != null && team.getMemberIds().contains(userId)) ||
                team.getStatus() == TeamStatus.FULL) {
                continue;
            }

            int score = 0;
            List<RecommendationReason> reasons = new ArrayList<>();

            // 1. Open Slots (+10)
            if (team.getCurrentMemberCount() < team.getMaxMembers()) {
                score += 10;
                reasons.add(new RecommendationReason("Team has open slots", 10));
            }

            boolean hasRequiredSkillMatch = false;
            boolean hasRequiredRoleMatch = false;

            if (team.getRequiredRoles() != null && !userSkills.isEmpty()) {
                for (RequiredRole role : team.getRequiredRoles()) {
                    if (role.getRequiredSkills() != null) {
                        List<String> roleSkills = role.getRequiredSkills().stream()
                                .map(String::toLowerCase)
                                .collect(Collectors.toList());

                        boolean match = roleSkills.stream().anyMatch(userSkills::contains);
                        if (match) {
                            hasRequiredSkillMatch = true;
                            // Check if this specific role has open slots for a Role Match
                            if (role.getSlots() > role.getFilledSlots()) {
                                hasRequiredRoleMatch = true;
                            }
                        }
                    }
                }
            }

            // 2. Required Skill Match (+30)
            if (hasRequiredSkillMatch) {
                score += 30;
                reasons.add(new RecommendationReason("Matches Team Required Skills", 30));
            }

            // 3. Required Role Match (+20)
            if (hasRequiredRoleMatch) {
                score += 20;
                reasons.add(new RecommendationReason("Matches Open Required Role", 20));
            }

            // 4. Team Completion (+20)
            if (team.getTeamCompletionPercentage() != null && team.getTeamCompletionPercentage() > 50) {
                score += 20;
                reasons.add(new RecommendationReason("Highly Active Team (>50% completion)", 20));
            }

            // 5. Hackathon Domain Match (+20)
            Hackathon hackathon = hackathonRepository.findById(team.getHackathonId()).orElse(null);
            if (hackathon != null && hackathon.getDomains() != null && !userInterests.isEmpty()) {
                List<String> hackathonDomains = hackathon.getDomains().stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
                
                if (hackathonDomains.stream().anyMatch(userInterests::contains)) {
                    score += 20;
                    reasons.add(new RecommendationReason("Hackathon Domain matches Interests", 20));
                }
            }

            // 6. Team Trust Score (+20)
            int leaderTrust = trustService.calculateTrustScore(team.getLeaderId()).getTrustScore();
            if (leaderTrust > 0) {
                int trustImpact = (int) Math.round((leaderTrust / 100.0) * 20.0);
                if (trustImpact > 0) {
                    score += trustImpact;
                    reasons.add(new RecommendationReason("Highly Trusted Team Leader (" + leaderTrust + "/100)", trustImpact));
                }
            }

            if (score > 0) {
                TeamSummaryResponse teamSummary = TeamSummaryResponse.builder()
                        .id(team.getId())
                        .name(team.getName())
                        .leaderId(team.getLeaderId())
                        .status(team.getStatus())
                        .maxMembers(team.getMaxMembers())
                        .currentMemberCount(team.getCurrentMemberCount())
                        .isOpen(team.getStatus() != TeamStatus.FULL)
                        .build();

                recommendations.add(TeamRecommendation.builder()
                        .team(teamSummary)
                        .matchScore(score)
                        .reasons(reasons)
                        .build());
            }
        }

        // Sort descending by match score
        recommendations.sort((a, b) -> Integer.compare(b.getMatchScore(), a.getMatchScore()));

        return recommendations.stream().limit(20).collect(Collectors.toList());
    }

    @Override
    public List<HackathonRecommendation> getHackathonRecommendations(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<String> userSkills = new ArrayList<>();
        List<String> userInterests = new ArrayList<>();
        boolean hasExperience = false;

        if (user.getProfile() != null) {
            if (user.getProfile().getSkills() != null) {
                userSkills = user.getProfile().getSkills().stream()
                        .map(Skill::getName)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
            }
            if (user.getProfile().getInterests() != null) {
                userInterests = user.getProfile().getInterests().stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
            }
            if (user.getProfile().getExperience() != null && !user.getProfile().getExperience().isEmpty()) {
                hasExperience = true;
            }
        }

        List<String> combinedUserTraits = new ArrayList<>(userSkills);
        combinedUserTraits.addAll(userInterests);

        List<Hackathon> allHackathons = hackathonRepository.findAll();
        List<HackathonRecommendation> recommendations = new ArrayList<>();

        for (Hackathon hackathon : allHackathons) {
            // Skip closed hackathons
            if (hackathon.getStatus() == HackathonStatus.COMPLETED || hackathon.getStatus() == HackathonStatus.CANCELLED) {
                continue;
            }

            int score = 0;
            List<RecommendationReason> reasons = new ArrayList<>();

            // 1. Skill Match (+30) - Hackathon Tech Stacks vs User Skills
            if (hackathon.getTechStacks() != null && !userSkills.isEmpty()) {
                boolean skillMatch = hackathon.getTechStacks().stream()
                        .map(String::toLowerCase)
                        .anyMatch(userSkills::contains);
                if (skillMatch) {
                    score += 30;
                    reasons.add(new RecommendationReason("Hackathon Tech Stack matches your skills", 30));
                }
            }

            // 2. Domain Match (+30) - Hackathon Domains vs User Interests
            if (hackathon.getDomains() != null && !userInterests.isEmpty()) {
                boolean domainMatch = hackathon.getDomains().stream()
                        .map(String::toLowerCase)
                        .anyMatch(userInterests::contains);
                if (domainMatch) {
                    score += 30;
                    reasons.add(new RecommendationReason("Hackathon Domain matches your interests", 30));
                }
            }

            // 3. Tags Match (+20) - Hackathon Tags vs Any User Trait
            if (hackathon.getTags() != null && !combinedUserTraits.isEmpty()) {
                boolean tagsMatch = hackathon.getTags().stream()
                        .map(String::toLowerCase)
                        .anyMatch(combinedUserTraits::contains);
                if (tagsMatch) {
                    score += 20;
                    reasons.add(new RecommendationReason("Hackathon Tags match your profile", 20));
                }
            }

            // 4. Previous Participation / Experience (+20)
            if (hasExperience) {
                score += 20;
                reasons.add(new RecommendationReason("Your prior experience is a great fit", 20));
            }

            if (score > 0) {
                recommendations.add(HackathonRecommendation.builder()
                        .hackathon(hackathon)
                        .matchScore(score)
                        .reasons(reasons)
                        .build());
            }
        }

        recommendations.sort((a, b) -> Integer.compare(b.getMatchScore(), a.getMatchScore()));

        return recommendations.stream().limit(20).collect(Collectors.toList());
    }
}
