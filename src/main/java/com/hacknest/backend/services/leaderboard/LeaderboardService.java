package com.hacknest.backend.services.leaderboard;

import com.hacknest.backend.dto.leaderboard.LeaderboardUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeaderboardService {
    Page<LeaderboardUserResponse> getGlobalLeaderboard(Pageable pageable);
    Page<LeaderboardUserResponse> getCollegeLeaderboard(String college, Pageable pageable);
    void syncLeaderboard();
}
