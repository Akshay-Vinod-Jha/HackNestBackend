package com.hacknest.backend.services.profile;

import com.hacknest.backend.dto.profile.*;
import com.hacknest.backend.dto.profile.*;
import com.hacknest.backend.dto.hackathon.HackathonSummaryResponse;
import com.hacknest.backend.dto.team.TeamSummaryResponse;
import com.hacknest.backend.enums.ApplicationStatus;
import com.hacknest.backend.enums.InvitationStatus;
import com.hacknest.backend.enums.TeamStatus;
import com.hacknest.backend.models.User;
import com.hacknest.backend.models.achievement.Achievement;
import com.hacknest.backend.models.hackathon.Hackathon;
import com.hacknest.backend.models.team.Team;
import com.hacknest.backend.models.profile.Experience;
import com.hacknest.backend.models.profile.PortfolioLink;
import com.hacknest.backend.models.profile.Profile;
import com.hacknest.backend.models.profile.Skill;
import com.hacknest.backend.repositories.AchievementRepository;
import com.hacknest.backend.repositories.ApplicationRepository;
import com.hacknest.backend.repositories.HackathonRepository;
import com.hacknest.backend.repositories.InvitationRepository;
import com.hacknest.backend.repositories.TeamRepository;
import com.hacknest.backend.repositories.UserRepository;
import com.hacknest.backend.services.trust.TrustService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final AchievementRepository achievementRepository;
    private final HackathonRepository hackathonRepository;
    private final ApplicationRepository applicationRepository;
    private final InvitationRepository invitationRepository;
    private final TrustService trustService;
    private final com.hacknest.backend.repositories.LeaderboardRepository leaderboardRepository;

    @Override
    public ProfileResponse getMyProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return buildProfileResponse(user);
    }

    @Override
    public ProfileResponse updateMyProfile(String userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            user.setProfile(profile);
        }

        profile.setHeadline(request.getHeadline());
        profile.setBio(request.getBio());
        profile.setCollege(request.getCollege());
        profile.setDegree(request.getDegree());
        profile.setBranch(request.getBranch());
        profile.setGraduationYear(request.getGraduationYear());

        if (request.getSkills() != null) {
            profile.setSkills(request.getSkills().stream().map(dto -> 
                Skill.builder()
                    .name(dto.getName())
                    .level(dto.getLevel())
                    .yearsOfExperience(dto.getYearsOfExperience())
                    .verified(false) 
                    .rating(0.0) 
                    .build()
            ).collect(Collectors.toList()));
        }

        if (request.getExperience() != null) {
            profile.setExperience(request.getExperience().stream().map(dto ->
                Experience.builder()
                    .title(dto.getTitle())
                    .organization(dto.getOrganization())
                    .description(dto.getDescription())
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .currentlyWorking(dto.isCurrentlyWorking())
                    .build()
            ).collect(Collectors.toList()));
        }

        if (request.getPortfolioLinks() != null) {
            profile.setPortfolioLinks(request.getPortfolioLinks().stream().map(dto ->
                PortfolioLink.builder()
                    .type(dto.getType())
                    .url(dto.getUrl())
                    .build()
            ).collect(Collectors.toList()));
        }

        profile.setProfileCompletionPercentage(calculateProfileCompletion(profile));

        user = userRepository.save(user);

        return buildProfileResponse(user);
    }

    private Integer calculateProfileCompletion(Profile profile) {
        int score = 0;
        if (profile.getHeadline() != null && !profile.getHeadline().isBlank()) score += 10;
        if (profile.getBio() != null && !profile.getBio().isBlank()) score += 10;
        if (profile.getCollege() != null && !profile.getCollege().isBlank()) score += 5;
        if (profile.getDegree() != null && !profile.getDegree().isBlank()) score += 5;
        if (profile.getBranch() != null && !profile.getBranch().isBlank()) score += 5;
        if (profile.getGraduationYear() != null) score += 5;
        if (profile.getSkills() != null && !profile.getSkills().isEmpty()) score += 20;
        if (profile.getExperience() != null && !profile.getExperience().isEmpty()) score += 20;
        if (profile.getPortfolioLinks() != null && !profile.getPortfolioLinks().isEmpty()) score += 20;
        return Math.min(score, 100);
    }

    private ProfileResponse buildProfileResponse(User user) {
        Profile profile = user.getProfile();
        return ProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .headline(profile != null ? profile.getHeadline() : null)
                .bio(profile != null ? profile.getBio() : null)
                .college(profile != null ? profile.getCollege() : null)
                .degree(profile != null ? profile.getDegree() : null)
                .branch(profile != null ? profile.getBranch() : null)
                .graduationYear(profile != null ? profile.getGraduationYear() : null)
                .profileCompletionPercentage(profile != null ? profile.getProfileCompletionPercentage() : 0)
                .skills(profile != null ? profile.getSkills() : new ArrayList<>())
                .experience(profile != null ? profile.getExperience() : new ArrayList<>())
                .portfolioLinks(profile != null ? profile.getPortfolioLinks() : new ArrayList<>())
                .build();
    }

    @Override
    public List<CompetitionHistoryResponse> getCompetitionHistory(String userId) {
        List<Team> teams = teamRepository.findByLeaderIdOrMemberIdsContaining(userId, userId);
        List<Achievement> achievements = achievementRepository.findByUserId(userId);
        
        Map<String, Achievement> achievementMap = achievements.stream()
                .filter(a -> a.getTeamId() != null)
                .collect(Collectors.toMap(Achievement::getTeamId, a -> a, (a1, a2) -> a1));

        List<CompetitionHistoryResponse> history = new ArrayList<>();
        
        for (Team team : teams) {
            Hackathon hackathon = hackathonRepository.findById(team.getHackathonId()).orElse(null);
            if (hackathon == null) continue;
            
            String role = userId.equals(team.getLeaderId()) ? "LEADER" : "MEMBER";
            Achievement achievement = achievementMap.get(team.getId());
            
            String result = "PARTICIPANT";
            if (achievement != null) {
                result = achievement.getType().name();
            } else if (team.getStatus() != TeamStatus.COMPLETED) {
                result = team.getStatus().name();
            }
            
            LocalDateTime participationDate = hackathon.getHackathonStartDate() != null ? hackathon.getHackathonStartDate() : hackathon.getCreatedAt();

            history.add(CompetitionHistoryResponse.builder()
                    .hackathon(buildHackathonSummary(hackathon))
                    .team(buildTeamSummary(team))
                    .role(role)
                    .result(result)
                    .participationDate(participationDate)
                    .build());
        }
        
        history.sort((h1, h2) -> {
            if (h1.getParticipationDate() == null) return 1;
            if (h2.getParticipationDate() == null) return -1;
            return h2.getParticipationDate().compareTo(h1.getParticipationDate());
        });
        
        return history;
    }

    private HackathonSummaryResponse buildHackathonSummary(Hackathon h) {
        return HackathonSummaryResponse.builder()
                .id(h.getId())
                .title(h.getTitle())
                .organizer(h.getOrganizer())
                .mode(h.getMode())
                .status(h.getStatus())
                .registrationDeadline(h.getRegistrationDeadline())
                .hackathonStartDate(h.getHackathonStartDate())
                .country(h.getCountry())
                .city(h.getCity())
                .tags(h.getTags())
                .build();
    }

    private TeamSummaryResponse buildTeamSummary(Team t) {
        return TeamSummaryResponse.builder()
                .id(t.getId())
                .name(t.getName())
                .leaderId(t.getLeaderId())
                .maxMembers(t.getMaxMembers())
                .currentMemberCount(t.getCurrentMemberCount())
                .isOpen(t.getIsOpen())
                .status(t.getStatus())
                .build();
    }

    @Override
    public List<TimelineEvent> getTimeline(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
                
        List<TimelineEvent> events = new ArrayList<>();
        
        // 1. Joined Platform
        if (user.getCreatedAt() != null) {
            events.add(TimelineEvent.builder()
                    .eventType("JOINED_PLATFORM")
                    .title("Joined HackNest")
                    .description("Welcome to the community!")
                    .date(user.getCreatedAt())
                    .build());
        }

        // 2. Teams (Joined Team, Became Leader, Participated Hackathon)
        List<Team> teams = teamRepository.findByLeaderIdOrMemberIdsContaining(userId, userId);
        for (Team team : teams) {
            boolean isLeader = userId.equals(team.getLeaderId());
            
            if (isLeader) {
                events.add(TimelineEvent.builder()
                        .eventType("BECAME_LEADER")
                        .title("Became Team Leader")
                        .description("Took charge of team: " + team.getName())
                        .date(team.getCreatedAt() != null ? team.getCreatedAt() : LocalDateTime.now())
                        .build());
            } else {
                events.add(TimelineEvent.builder()
                        .eventType("JOINED_TEAM")
                        .title("Joined Team")
                        .description("Joined team: " + team.getName())
                        .date(team.getCreatedAt() != null ? team.getCreatedAt() : LocalDateTime.now())
                        .build());
            }
            
            // Hackathon Participation
            if (team.getHackathonId() != null) {
                Hackathon hackathon = hackathonRepository.findById(team.getHackathonId()).orElse(null);
                if (hackathon != null) {
                    events.add(TimelineEvent.builder()
                            .eventType("PARTICIPATED_HACKATHON")
                            .title("Participated in " + hackathon.getTitle())
                            .description("Competed with team: " + team.getName())
                            .date(hackathon.getHackathonStartDate() != null ? hackathon.getHackathonStartDate() : hackathon.getCreatedAt())
                            .build());
                }
            }
        }
        
        // 3. Achievements
        List<Achievement> achievements = achievementRepository.findByUserId(userId);
        for (Achievement achievement : achievements) {
            events.add(TimelineEvent.builder()
                    .eventType("ACHIEVEMENT_WON")
                    .title("Earned " + achievement.getType().name())
                    .description(achievement.getTitle() != null ? achievement.getTitle() : "Achievement unlocked")
                    .date(achievement.getAchievedAt() != null ? achievement.getAchievedAt() : LocalDateTime.now())
                    .build());
        }
        
        // Sort descending
        events.sort((e1, e2) -> {
            if (e1.getDate() == null) return 1;
            if (e2.getDate() == null) return -1;
            return e2.getDate().compareTo(e1.getDate());
        });
        
        return events;
    }

    @Override
    public ProfileAnalyticsResponse getAnalytics(String userId) {
        long totalTeamsLed = teamRepository.countByLeaderId(userId);
        long totalTeamsJoined = teamRepository.countByMemberIdsContains(userId);
        long totalHackathons = totalTeamsLed + totalTeamsJoined;
        
        long totalApplications = applicationRepository.countByApplicantId(userId);
        long acceptedApplications = applicationRepository.countByApplicantIdAndStatus(userId, ApplicationStatus.ACCEPTED);
        
        long totalInvitations = invitationRepository.countByReceiverId(userId);
        long acceptedInvitations = invitationRepository.countByReceiverIdAndStatus(userId, InvitationStatus.ACCEPTED);
        
        long totalAchievements = achievementRepository.countByUserId(userId);
        
        int trustScore = trustService.calculateTrustScore(userId).getTrustScore();
        
        long higherRankers = leaderboardRepository.countByTrustScoreGreaterThan(trustScore);
        int globalRank = (int) higherRankers + 1;

        return ProfileAnalyticsResponse.builder()
                .userId(userId)
                .totalHackathons(totalHackathons)
                .totalTeamsJoined(totalTeamsJoined)
                .totalTeamsLed(totalTeamsLed)
                .totalApplications(totalApplications)
                .acceptedApplications(acceptedApplications)
                .totalInvitations(totalInvitations)
                .acceptedInvitations(acceptedInvitations)
                .totalAchievements(totalAchievements)
                .trustScore(trustScore)
                .globalRank(globalRank)
                .build();
    }
}
