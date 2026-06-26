package com.hacknest.backend.services.profile;

import com.hacknest.backend.dto.profile.ProfileResponse;
import com.hacknest.backend.dto.profile.UpdateProfileRequest;
import com.hacknest.backend.dto.profile.CompetitionHistoryResponse;
import com.hacknest.backend.dto.profile.ProfileAnalyticsResponse;
import com.hacknest.backend.dto.profile.TimelineEvent;

import java.util.List;

public interface ProfileService {
    ProfileResponse getMyProfile(String userId);
    ProfileResponse updateMyProfile(String userId, UpdateProfileRequest request);
    List<CompetitionHistoryResponse> getCompetitionHistory(String userId);
    List<TimelineEvent> getTimeline(String userId);
    ProfileAnalyticsResponse getAnalytics(String userId);
}
