package com.hacknest.backend.dto.profile;

import com.hacknest.backend.enums.SkillLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SkillDto {
    @NotBlank(message = "Skill name is required")
    private String name;
    
    private SkillLevel level;
    
    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;
}
