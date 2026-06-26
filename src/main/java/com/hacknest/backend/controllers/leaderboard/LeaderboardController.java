package com.hacknest.backend.controllers.leaderboard;

import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.dto.leaderboard.LeaderboardUserResponse;
import com.hacknest.backend.services.leaderboard.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/global")
    public ResponseEntity<ApiResponse<Page<LeaderboardUserResponse>>> getGlobalLeaderboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
            
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "trustScore", "achievements", "participation"));
        Page<LeaderboardUserResponse> response = leaderboardService.getGlobalLeaderboard(pageable);
        return ResponseEntity.ok(ApiResponse.success("Global leaderboard fetched successfully", response));
    }

    @GetMapping("/college")
    public ResponseEntity<ApiResponse<Page<LeaderboardUserResponse>>> getCollegeLeaderboard(
            @RequestParam String college,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
            
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "trustScore", "achievements", "participation"));
        Page<LeaderboardUserResponse> response = leaderboardService.getCollegeLeaderboard(college, pageable);
        return ResponseEntity.ok(ApiResponse.success("College leaderboard fetched successfully", response));
    }
    
    @GetMapping("/sync")
    public ResponseEntity<ApiResponse<String>> syncLeaderboard() {
        leaderboardService.syncLeaderboard();
        return ResponseEntity.ok(ApiResponse.success("Leaderboard sync triggered successfully", "OK"));
    }
}
