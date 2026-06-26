package com.hacknest.backend.controllers.profile;

import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.dto.profile.CompetitionHistoryResponse;
import com.hacknest.backend.dto.profile.ProfileAnalyticsResponse;
import com.hacknest.backend.dto.profile.ProfileResponse;
import com.hacknest.backend.dto.profile.TimelineEvent;
import com.hacknest.backend.dto.profile.UpdateProfileRequest;
import com.hacknest.backend.models.User;
import com.hacknest.backend.services.profile.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(@AuthenticationPrincipal User user) {
        ProfileResponse response = profileService.getMyProfile(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", response));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateMyProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateProfileRequest request) {
        ProfileResponse response = profileService.updateMyProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<CompetitionHistoryResponse>>> getMyHistory(@AuthenticationPrincipal User user) {
        List<CompetitionHistoryResponse> response = profileService.getCompetitionHistory(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Competition history fetched successfully", response));
    }

    @GetMapping("/timeline")
    public ResponseEntity<ApiResponse<List<TimelineEvent>>> getMyTimeline(@AuthenticationPrincipal User user) {
        List<TimelineEvent> response = profileService.getTimeline(user.getId());
        return ResponseEntity.ok(ApiResponse.success("User timeline fetched successfully", response));
    }

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<ProfileAnalyticsResponse>> getMyAnalytics(@AuthenticationPrincipal User user) {
        ProfileAnalyticsResponse response = profileService.getAnalytics(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Profile analytics fetched successfully", response));
    }
}
