package com.hacknest.backend.dto.team;

import com.hacknest.backend.enums.TeamStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeamSummaryResponse {
    private String id;
    private String name;
    private String leaderId;
    private List<RequiredRoleSummaryDto> requiredRoles;
    private Integer maxMembers;
    private Integer currentMemberCount;
    private Boolean isOpen;
    private TeamStatus status;
}
