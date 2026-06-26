package com.hacknest.backend.dto.team;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class RequiredRoleDto {
    @NotBlank(message = "Role name is required")
    private String roleName;

    private List<String> requiredSkills;

    @Min(value = 1, message = "At least one slot must be required")
    private Integer slots;
}
