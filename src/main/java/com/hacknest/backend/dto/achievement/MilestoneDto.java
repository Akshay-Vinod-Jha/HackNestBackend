package com.hacknest.backend.dto.achievement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneDto {
    private long participationCount;
    private long winnerCount;
    private long runnerUpCount;
    private long top10Count;
    private long teamLeaderCount;
    private long specialMentionCount;
}
