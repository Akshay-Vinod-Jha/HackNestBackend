package com.hacknest.backend.dto.recommendation;

import com.hacknest.backend.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeammateRecommendation {
    private User user;
    private int matchScore;
    private List<RecommendationReason> reasons;
}
