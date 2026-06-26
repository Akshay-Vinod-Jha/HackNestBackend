package com.hacknest.backend.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRatingSummaryResponse {
    private String userId;
    private int totalRatings;
    private Double averageReliability;
    private Double averageContribution;
    private Map<String, Double> averageSkillRatings;
}
