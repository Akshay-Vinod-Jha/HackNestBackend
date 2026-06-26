package com.hacknest.backend.dto.rating;

import com.hacknest.backend.models.rating.SkillRating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {
    private String id;
    private String hackathonId;
    private String teamId;
    private String raterId;
    private String ratedUserId;
    private List<SkillRating> skillRatings;
    private Integer reliabilityRating;
    private Integer contributionRating;
    private String comment;
    private LocalDateTime createdAt;
}
