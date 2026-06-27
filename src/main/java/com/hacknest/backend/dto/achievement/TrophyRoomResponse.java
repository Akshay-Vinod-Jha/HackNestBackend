package com.hacknest.backend.dto.achievement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrophyRoomResponse {
    private MilestoneDto milestones;
    private List<BadgeDto> badges;
    private List<CertificateDto> certificates;
}
