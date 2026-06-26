package com.hacknest.backend.dto.team;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class TeamAnalysisResponse {
    private int alignmentScore;
    private List<String> missingRoles;
    private List<String> missingSkills;
}
