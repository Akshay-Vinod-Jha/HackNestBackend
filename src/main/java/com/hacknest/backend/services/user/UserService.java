package com.hacknest.backend.services.user;

import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.dto.user.UserSummaryResponse;
import com.hacknest.backend.enums.SkillLevel;

import java.util.List;

public interface UserService {
    PagedResponse<UserSummaryResponse> searchUsers(
            List<String> skills,
            String college,
            Integer graduationYear,
            Integer profileCompletionMin,
            SkillLevel skillLevel,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );
}
