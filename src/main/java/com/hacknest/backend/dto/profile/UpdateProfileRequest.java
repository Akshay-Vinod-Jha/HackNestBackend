package com.hacknest.backend.dto.profile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class UpdateProfileRequest {
    
    private String headline;
    
    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;
    
    private String college;
    private String degree;
    private String branch;
    
    @Min(1970)
    @Max(2100)
    private Integer graduationYear;
    
    @Valid
    private List<SkillDto> skills;
    
    @Valid
    private List<ExperienceDto> experience;
    
    @Valid
    private List<PortfolioLinkDto> portfolioLinks;
}
