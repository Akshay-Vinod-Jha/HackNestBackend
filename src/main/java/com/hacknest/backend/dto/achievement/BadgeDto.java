package com.hacknest.backend.dto.achievement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeDto {
    private String title;
    private boolean isEarned;
    private Integer level;
    private String colorClass;
}
