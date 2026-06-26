package com.hacknest.backend.services.profile;

import com.hacknest.backend.dto.profile.*;
import com.hacknest.backend.models.User;
import com.hacknest.backend.models.profile.Experience;
import com.hacknest.backend.models.profile.PortfolioLink;
import com.hacknest.backend.models.profile.Profile;
import com.hacknest.backend.models.profile.Skill;
import com.hacknest.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    @Override
    public ProfileResponse getMyProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return buildProfileResponse(user);
    }

    @Override
    public ProfileResponse updateMyProfile(String userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            user.setProfile(profile);
        }

        profile.setHeadline(request.getHeadline());
        profile.setBio(request.getBio());
        profile.setCollege(request.getCollege());
        profile.setDegree(request.getDegree());
        profile.setBranch(request.getBranch());
        profile.setGraduationYear(request.getGraduationYear());

        if (request.getSkills() != null) {
            profile.setSkills(request.getSkills().stream().map(dto -> 
                Skill.builder()
                    .name(dto.getName())
                    .level(dto.getLevel())
                    .yearsOfExperience(dto.getYearsOfExperience())
                    .verified(false) 
                    .rating(0.0) 
                    .build()
            ).collect(Collectors.toList()));
        }

        if (request.getExperience() != null) {
            profile.setExperience(request.getExperience().stream().map(dto ->
                Experience.builder()
                    .title(dto.getTitle())
                    .organization(dto.getOrganization())
                    .description(dto.getDescription())
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .currentlyWorking(dto.isCurrentlyWorking())
                    .build()
            ).collect(Collectors.toList()));
        }

        if (request.getPortfolioLinks() != null) {
            profile.setPortfolioLinks(request.getPortfolioLinks().stream().map(dto ->
                PortfolioLink.builder()
                    .type(dto.getType())
                    .url(dto.getUrl())
                    .build()
            ).collect(Collectors.toList()));
        }

        profile.setProfileCompletionPercentage(calculateCompletion(profile));

        user = userRepository.save(user);

        return buildProfileResponse(user);
    }

    private Integer calculateCompletion(Profile profile) {
        int score = 0;
        if (profile.getHeadline() != null && !profile.getHeadline().isBlank()) score += 10;
        if (profile.getBio() != null && !profile.getBio().isBlank()) score += 10;
        if (profile.getCollege() != null && !profile.getCollege().isBlank()) score += 20;
        if (profile.getSkills() != null && !profile.getSkills().isEmpty()) score += 20;
        if (profile.getExperience() != null && !profile.getExperience().isEmpty()) score += 20;
        if (profile.getPortfolioLinks() != null && !profile.getPortfolioLinks().isEmpty()) score += 20;
        return Math.min(score, 100);
    }

    private ProfileResponse buildProfileResponse(User user) {
        Profile profile = user.getProfile();
        return ProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .headline(profile != null ? profile.getHeadline() : null)
                .bio(profile != null ? profile.getBio() : null)
                .college(profile != null ? profile.getCollege() : null)
                .degree(profile != null ? profile.getDegree() : null)
                .branch(profile != null ? profile.getBranch() : null)
                .graduationYear(profile != null ? profile.getGraduationYear() : null)
                .profileCompletionPercentage(profile != null ? profile.getProfileCompletionPercentage() : 0)
                .skills(profile != null ? profile.getSkills() : new ArrayList<>())
                .experience(profile != null ? profile.getExperience() : new ArrayList<>())
                .portfolioLinks(profile != null ? profile.getPortfolioLinks() : new ArrayList<>())
                .build();
    }
}
