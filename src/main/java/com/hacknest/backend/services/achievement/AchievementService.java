package com.hacknest.backend.services.achievement;

import com.hacknest.backend.dto.achievement.BadgeDto;
import com.hacknest.backend.dto.achievement.CertificateDto;
import com.hacknest.backend.dto.achievement.MilestoneDto;
import com.hacknest.backend.dto.achievement.TrophyRoomResponse;
import com.hacknest.backend.models.achievement.Achievement;
import com.hacknest.backend.models.achievement.AchievementType;
import com.hacknest.backend.models.rating.Rating;
import com.hacknest.backend.models.rating.SkillRating;
import com.hacknest.backend.repositories.AchievementRepository;
import com.hacknest.backend.repositories.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final RatingRepository ratingRepository;

    public TrophyRoomResponse getTrophyRoom(String userId) {
        List<Achievement> achievements = achievementRepository.findByUserId(userId);
        
        MilestoneDto milestones = calculateMilestones(achievements);
        List<CertificateDto> certificates = extractCertificates(achievements);
        List<BadgeDto> badges = generateSkillBadges(userId);
        
        return TrophyRoomResponse.builder()
                .milestones(milestones)
                .badges(badges)
                .certificates(certificates)
                .build();
    }

    private MilestoneDto calculateMilestones(List<Achievement> achievements) {
        long participationCount = achievements.stream().filter(a -> a.getType() == AchievementType.PARTICIPATION).count();
        long winnerCount = achievements.stream().filter(a -> a.getType() == AchievementType.WINNER).count();
        long runnerUpCount = achievements.stream().filter(a -> a.getType() == AchievementType.RUNNER_UP).count();
        long top10Count = achievements.stream().filter(a -> a.getType() == AchievementType.TOP_10).count();
        long teamLeaderCount = achievements.stream().filter(a -> a.getType() == AchievementType.TEAM_LEADER).count();
        long specialMentionCount = achievements.stream().filter(a -> a.getType() == AchievementType.SPECIAL_MENTION).count();
        
        return MilestoneDto.builder()
                .participationCount(participationCount)
                .winnerCount(winnerCount)
                .runnerUpCount(runnerUpCount)
                .top10Count(top10Count)
                .teamLeaderCount(teamLeaderCount)
                .specialMentionCount(specialMentionCount)
                .build();
    }

    private List<CertificateDto> extractCertificates(List<Achievement> achievements) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        return achievements.stream()
                .filter(a -> a.getType() == AchievementType.CERTIFICATION)
                .map(a -> CertificateDto.builder()
                        .title(a.getTitle())
                        .issuer(a.getDescription() != null ? a.getDescription() : "HackNest Official") // Using description as issuer for now
                        .date(a.getAchievedAt() != null ? a.getAchievedAt().format(formatter).toUpperCase() : "N/A")
                        .verifyUrl(a.getCertificateUrl())
                        .build())
                .collect(Collectors.toList());
    }

    private List<BadgeDto> generateSkillBadges(String userId) {
        List<Rating> userRatings = ratingRepository.findByRatedUserId(userId);
        
        Map<String, List<Integer>> skillScores = new HashMap<>();
        
        for (Rating rating : userRatings) {
            if (rating.getSkillRatings() != null) {
                for (SkillRating sr : rating.getSkillRatings()) {
                    skillScores.computeIfAbsent(sr.getSkillName(), k -> new ArrayList<>()).add(sr.getRating());
                }
            }
        }
        
        List<BadgeDto> badges = new ArrayList<>();
        
        // Frontend Master Badge
        badges.add(createBadgeFromSkill("Frontend", "Frontend Master", skillScores, "from-blue-500 to-indigo-600"));
        // Backend Guru Badge
        badges.add(createBadgeFromSkill("Backend", "Backend Guru", skillScores, "from-emerald-500 to-teal-600"));
        // Design Wizard Badge
        badges.add(createBadgeFromSkill("UI/UX", "Design Wizard", skillScores, "from-purple-500 to-pink-600"));
        // Top Presenter Badge
        badges.add(createBadgeFromSkill("Presentation", "Top Presenter", skillScores, "from-amber-500 to-orange-600"));
        
        return badges;
    }
    
    private BadgeDto createBadgeFromSkill(String skillName, String badgeTitle, Map<String, List<Integer>> skillScores, String colorClass) {
        List<Integer> scores = skillScores.getOrDefault(skillName, new ArrayList<>());
        
        boolean isEarned = false;
        int level = 0;
        
        if (!scores.isEmpty()) {
            double avg = scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            if (avg >= 4.0) {
                isEarned = true;
                level = 3;
            } else if (avg >= 3.0) {
                isEarned = true;
                level = 2;
            } else if (avg >= 2.0) {
                isEarned = true;
                level = 1;
            }
        }
        
        return BadgeDto.builder()
                .title(badgeTitle)
                .isEarned(isEarned)
                .level(level)
                .colorClass(colorClass)
                .build();
    }
}
