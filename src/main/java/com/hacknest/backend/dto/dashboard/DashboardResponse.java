package com.hacknest.backend.dto.dashboard;

import com.hacknest.backend.dto.application.ApplicationResponse;
import com.hacknest.backend.dto.invitation.InvitationResponse;
import com.hacknest.backend.dto.profile.ProfileAnalyticsResponse;
import com.hacknest.backend.dto.recommendation.HackathonRecommendation;
import com.hacknest.backend.dto.recommendation.TeamRecommendation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private List<TeamRecommendation> recommendedTeams;
    private List<HackathonRecommendation> recommendedHackathons;
    private List<InvitationResponse> pendingInvitations;
    private List<ApplicationResponse> pendingApplications;
    private int profileCompletionPercentage;
    private int trustScore;
    private ProfileAnalyticsResponse analyticsSummary;
}
