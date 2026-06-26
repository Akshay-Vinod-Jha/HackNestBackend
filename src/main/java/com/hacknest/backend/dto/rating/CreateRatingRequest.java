package com.hacknest.backend.dto.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRatingRequest {

    @NotBlank(message = "Team ID is required")
    private String teamId;

    @NotBlank(message = "Rated User ID is required")
    private String ratedUserId;

    @NotNull(message = "Reliability rating is required")
    @Min(value = 1, message = "Reliability rating must be at least 1")
    @Max(value = 5, message = "Reliability rating cannot exceed 5")
    private Integer reliabilityRating;

    @NotNull(message = "Contribution rating is required")
    @Min(value = 1, message = "Contribution rating must be at least 1")
    @Max(value = 5, message = "Contribution rating cannot exceed 5")
    private Integer contributionRating;

    private List<SkillRatingRequest> skillRatings;

    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;
}
