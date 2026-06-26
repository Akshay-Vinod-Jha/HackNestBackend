package com.hacknest.backend.services.leaderboard;

import com.hacknest.backend.dto.leaderboard.LeaderboardUserResponse;
import com.hacknest.backend.dto.profile.ProfileAnalyticsResponse;
import com.hacknest.backend.models.User;
import com.hacknest.backend.models.leaderboard.LeaderboardEntry;
import com.hacknest.backend.repositories.LeaderboardRepository;
import com.hacknest.backend.repositories.UserRepository;
import com.hacknest.backend.services.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;
    private final UserRepository userRepository;
    private final ProfileService profileService;

    @Override
    public Page<LeaderboardUserResponse> getGlobalLeaderboard(Pageable pageable) {
        return leaderboardRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public Page<LeaderboardUserResponse> getCollegeLeaderboard(String college, Pageable pageable) {
        return leaderboardRepository.findByCollegeIgnoreCase(college, pageable).map(this::mapToResponse);
    }
    
    private LeaderboardUserResponse mapToResponse(LeaderboardEntry entry) {
        return LeaderboardUserResponse.builder()
                .userId(entry.getUserId())
                .fullName(entry.getFullName())
                .college(entry.getCollege())
                .trustScore(entry.getTrustScore())
                .achievements(entry.getAchievements())
                .participation(entry.getParticipation())
                .contribution(entry.getContribution())
                .globalRank(0) // Rank offset managed by client/pagination
                .build();
    }

    // Sync leaderboard fully every hour
    @Scheduled(fixedRate = 3600000)
    @Override
    public void syncLeaderboard() {
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            try {
                ProfileAnalyticsResponse analytics = profileService.getAnalytics(user.getId());
                
                String college = null;
                if (user.getProfile() != null) {
                    college = user.getProfile().getCollege();
                }
                
                Optional<LeaderboardEntry> existing = leaderboardRepository.findByUserId(user.getId());
                LeaderboardEntry entry = existing.orElse(LeaderboardEntry.builder().userId(user.getId()).build());
                
                entry.setFullName(user.getFullName());
                entry.setCollege(college);
                entry.setTrustScore(analytics.getTrustScore());
                entry.setAchievements((int) analytics.getTotalAchievements());
                entry.setParticipation((int) analytics.getTotalHackathons());
                entry.setContribution((int) (analytics.getTotalTeamsLed() * 2 + analytics.getAcceptedApplications()));
                entry.setUpdatedAt(LocalDateTime.now());
                
                leaderboardRepository.save(entry);
            } catch (Exception e) {
                System.err.println("Failed to sync leaderboard for user: " + user.getId());
            }
        }
    }
}
