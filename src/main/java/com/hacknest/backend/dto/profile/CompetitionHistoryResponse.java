package com.hacknest.backend.dto.profile;

import com.hacknest.backend.dto.hackathon.HackathonSummaryResponse;
import com.hacknest.backend.dto.team.TeamSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionHistoryResponse {
    private HackathonSummaryResponse hackathon;
    private TeamSummaryResponse team;
    private String role;
    private String result;
    private LocalDateTime participationDate;
}
