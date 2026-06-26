package com.hacknest.backend.dto.team;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequiredRoleSummaryDto {
    private String roleName;
    private Integer slots;
    private Integer filledSlots;
}
