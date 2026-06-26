package com.hacknest.backend.models.profile;

import com.hacknest.backend.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Skill {
    private String name;
    private SkillLevel level;
    private Integer yearsOfExperience;
    
    @Builder.Default
    private boolean verified = false;
    
    private Double rating;
}
