package com.hacknest.backend.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileAnalyticsResponse {
    private String userId;
    private long totalHackathons;
    private long totalTeamsJoined;
    private long totalTeamsLed;
    private long totalApplications;
    private long acceptedApplications;
    private long totalInvitations;
    private long acceptedInvitations;
    private long totalAchievements;
    private int trustScore;
}
