package com.hacknest.backend.dto.team;

import com.hacknest.backend.enums.TeamStatus;
import com.hacknest.backend.models.team.RequiredRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TeamResponse {
    private String id;
    private String name;
    private String description;
    private String hackathonId;
    private String leaderId;
    private List<String> memberIds;
    private List<RequiredRole> requiredRoles;
    private List<String> requiredSkills;
    private Integer maxMembers;
    private Integer currentMemberCount;
    private Boolean isOpen;
    private TeamStatus status;
    private Integer teamCompletionPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
