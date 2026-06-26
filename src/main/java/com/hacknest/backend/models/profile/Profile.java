package com.hacknest.backend.models.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    private String headline;
    private String bio;
    private String college;
    private String degree;
    private String branch;
    private Integer graduationYear;
    private Integer profileCompletionPercentage;

    @Builder.Default
    private List<String> interests = new ArrayList<>();

    @Builder.Default
    private List<Skill> skills = new ArrayList<>();

    @Builder.Default
    private List<Experience> experience = new ArrayList<>();

    @Builder.Default
    private List<PortfolioLink> portfolioLinks = new ArrayList<>();
}
