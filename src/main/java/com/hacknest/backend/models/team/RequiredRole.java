package com.hacknest.backend.models.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequiredRole {
    private String roleName;
    private List<String> requiredSkills;
    private Integer slots;
    private Integer filledSlots;
}
