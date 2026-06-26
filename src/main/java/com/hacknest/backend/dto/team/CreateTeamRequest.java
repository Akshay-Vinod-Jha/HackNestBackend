package com.hacknest.backend.dto.team;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateTeamRequest {

    @NotBlank(message = "Team name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Hackathon ID is required")
    private String hackathonId;

    @NotNull(message = "Max members is required")
    @Min(value = 2, message = "Team must have capacity for at least 2 members")
    private Integer maxMembers;

    @NotEmpty(message = "At least one role must be required")
    @Valid
    private List<RequiredRoleDto> requiredRoles;

    private List<String> requiredSkills;
}
