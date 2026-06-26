package com.hacknest.backend.services.rating;

import com.hacknest.backend.dto.rating.CreateRatingRequest;
import com.hacknest.backend.dto.rating.RatingResponse;
import com.hacknest.backend.dto.rating.SkillRatingRequest;
import com.hacknest.backend.dto.rating.UserRatingSummaryResponse;
import com.hacknest.backend.enums.HackathonStatus;
import com.hacknest.backend.models.hackathon.Hackathon;
import com.hacknest.backend.models.rating.Rating;
import com.hacknest.backend.models.rating.SkillRating;
import com.hacknest.backend.models.team.Team;
import com.hacknest.backend.repositories.HackathonRepository;
import com.hacknest.backend.repositories.RatingRepository;
import com.hacknest.backend.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;

    @Override
    public RatingResponse createRating(CreateRatingRequest request, String raterId) {
        if (raterId.equals(request.getRatedUserId())) {
            throw new IllegalArgumentException("Users cannot rate themselves");
        }

        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        // Validate both users belong to the team
        boolean raterInTeam = team.getLeaderId().equals(raterId) || (team.getMemberIds() != null && team.getMemberIds().contains(raterId));
        boolean ratedInTeam = team.getLeaderId().equals(request.getRatedUserId()) || (team.getMemberIds() != null && team.getMemberIds().contains(request.getRatedUserId()));

        if (!raterInTeam || !ratedInTeam) {
            throw new IllegalArgumentException("Both users must be members of the same team to leave a rating");
        }

        Hackathon hackathon = hackathonRepository.findById(team.getHackathonId())
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if (hackathon.getStatus() != HackathonStatus.COMPLETED) {
            throw new IllegalArgumentException("Ratings can only be submitted after the hackathon is completely finished");
        }

        if (ratingRepository.existsByRaterIdAndRatedUserIdAndTeamId(raterId, request.getRatedUserId(), team.getId())) {
            throw new IllegalArgumentException("You have already submitted a rating for this user on this team");
        }

        List<SkillRating> skillRatings = new ArrayList<>();
        if (request.getSkillRatings() != null) {
            skillRatings = request.getSkillRatings().stream()
                    .map(sr -> SkillRating.builder()
                            .skillName(sr.getSkillName())
                            .rating(sr.getRating())
                            .build())
                    .collect(Collectors.toList());
        }

        Rating rating = Rating.builder()
                .hackathonId(hackathon.getId())
                .teamId(team.getId())
                .raterId(raterId)
                .ratedUserId(request.getRatedUserId())
                .reliabilityRating(request.getReliabilityRating())
                .contributionRating(request.getContributionRating())
                .skillRatings(skillRatings)
                .comment(request.getComment())
                .build();

        Rating savedRating = ratingRepository.save(rating);

        return RatingResponse.builder()
                .id(savedRating.getId())
                .hackathonId(savedRating.getHackathonId())
                .teamId(savedRating.getTeamId())
                .raterId(savedRating.getRaterId())
                .ratedUserId(savedRating.getRatedUserId())
                .skillRatings(savedRating.getSkillRatings())
                .reliabilityRating(savedRating.getReliabilityRating())
                .contributionRating(savedRating.getContributionRating())
                .comment(savedRating.getComment())
                .createdAt(savedRating.getCreatedAt())
                .build();
    }

    @Override
    public UserRatingSummaryResponse getUserRatingSummary(String userId) {
        List<Rating> ratings = ratingRepository.findByRatedUserId(userId);
        
        if (ratings == null || ratings.isEmpty()) {
            return UserRatingSummaryResponse.builder()
                    .userId(userId)
                    .totalRatings(0)
                    .averageReliability(0.0)
                    .averageContribution(0.0)
                    .averageSkillRatings(new HashMap<>())
                    .build();
        }

        int total = ratings.size();
        double sumReliability = 0;
        double sumContribution = 0;
        
        Map<String, List<Integer>> skillScores = new HashMap<>();

        for (Rating rating : ratings) {
            if (rating.getReliabilityRating() != null) sumReliability += rating.getReliabilityRating();
            if (rating.getContributionRating() != null) sumContribution += rating.getContributionRating();
            
            if (rating.getSkillRatings() != null) {
                for (SkillRating sr : rating.getSkillRatings()) {
                    skillScores.computeIfAbsent(sr.getSkillName().toLowerCase(), k -> new ArrayList<>()).add(sr.getRating());
                }
            }
        }

        Map<String, Double> avgSkillRatings = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : skillScores.entrySet()) {
            double avg = entry.getValue().stream().mapToInt(Integer::intValue).average().orElse(0.0);
            avgSkillRatings.put(entry.getKey(), Math.round(avg * 10.0) / 10.0);
        }

        return UserRatingSummaryResponse.builder()
                .userId(userId)
                .totalRatings(total)
                .averageReliability(Math.round((sumReliability / total) * 10.0) / 10.0)
                .averageContribution(Math.round((sumContribution / total) * 10.0) / 10.0)
                .averageSkillRatings(avgSkillRatings)
                .build();
    }
}
