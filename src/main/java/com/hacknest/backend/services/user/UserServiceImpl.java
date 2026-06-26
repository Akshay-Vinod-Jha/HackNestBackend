package com.hacknest.backend.services.user;

import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.dto.user.UserSummaryResponse;
import com.hacknest.backend.enums.SkillLevel;
import com.hacknest.backend.models.User;
import com.hacknest.backend.models.profile.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final MongoTemplate mongoTemplate;

    @Override
    public PagedResponse<UserSummaryResponse> searchUsers(
            List<String> skills,
            String college,
            Integer graduationYear,
            Integer profileCompletionMin,
            SkillLevel skillLevel,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Query query = new Query();

        if (skills != null && !skills.isEmpty()) {
            query.addCriteria(Criteria.where("profile.skills.name").in(skills));
        }

        if (StringUtils.hasText(college)) {
            query.addCriteria(Criteria.where("profile.college").regex(college, "i"));
        }

        if (graduationYear != null) {
            query.addCriteria(Criteria.where("profile.graduationYear").is(graduationYear));
        }

        if (profileCompletionMin != null) {
            query.addCriteria(Criteria.where("profile.profileCompletionPercentage").gte(profileCompletionMin));
        }

        if (skillLevel != null) {
            query.addCriteria(Criteria.where("profile.skills.level").is(skillLevel));
        }

        long total = mongoTemplate.count(query, User.class);

        query.with(pageable);
        List<User> users = mongoTemplate.find(query, User.class);

        List<UserSummaryResponse> content = users.stream().map(user -> {
            String headline = user.getProfile() != null ? user.getProfile().getHeadline() : null;
            String userCollege = user.getProfile() != null ? user.getProfile().getCollege() : null;
            Integer gradYear = user.getProfile() != null ? user.getProfile().getGraduationYear() : null;
            Integer completion = user.getProfile() != null ? user.getProfile().getProfileCompletionPercentage() : null;
            
            List<String> topSkills = List.of();
            if (user.getProfile() != null && user.getProfile().getSkills() != null) {
                topSkills = user.getProfile().getSkills().stream()
                        .map(Skill::getName)
                        .limit(5)
                        .collect(Collectors.toList());
            }

            return UserSummaryResponse.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .headline(headline)
                    .college(userCollege)
                    .graduationYear(gradYear)
                    .profileCompletionPercentage(completion)
                    .topSkills(topSkills)
                    .build();
        }).collect(Collectors.toList());

        Page<UserSummaryResponse> pageResult = new PageImpl<>(content, pageable, total);
        return PagedResponse.of(pageResult);
    }
}
