package com.hacknest.backend.dto.profile;

import com.hacknest.backend.models.profile.Experience;
import com.hacknest.backend.models.profile.PortfolioLink;
import com.hacknest.backend.models.profile.Skill;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProfileResponse {
    private String id;
    private String fullName;
    private String email;
    private String headline;
    private String bio;
    private String college;
    private String degree;
    private String branch;
    private Integer graduationYear;
    private Integer profileCompletionPercentage;
    private List<Skill> skills;
    private List<Experience> experience;
    private List<PortfolioLink> portfolioLinks;
}
