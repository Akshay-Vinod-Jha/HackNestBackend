package com.hacknest.backend.dto.user;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UserSummaryResponse {
    private String id;
    private String fullName;
    private String headline;
    private String college;
    private Integer graduationYear;
    private Integer profileCompletionPercentage;
    private List<String> topSkills;
}
