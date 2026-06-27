package com.hacknest.backend.controllers.achievement;

import com.hacknest.backend.dto.achievement.TrophyRoomResponse;
import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.models.User;
import com.hacknest.backend.services.achievement.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping("/trophy-room")
    public ResponseEntity<ApiResponse<TrophyRoomResponse>> getMyTrophyRoom(@AuthenticationPrincipal User user) {
        TrophyRoomResponse response = achievementService.getTrophyRoom(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Trophy room fetched successfully", response));
    }
}
