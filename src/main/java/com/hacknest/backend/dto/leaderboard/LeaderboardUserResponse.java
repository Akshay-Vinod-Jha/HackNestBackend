package com.hacknest.backend.dto.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardUserResponse {
    private String userId;
    private String fullName;
    private String college;
    private int trustScore;
    private int achievements;
    private int participation;
    private int contribution;
    private int globalRank;
}
