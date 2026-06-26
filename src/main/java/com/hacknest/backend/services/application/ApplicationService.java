package com.hacknest.backend.services.application;

import com.hacknest.backend.dto.application.ApplicationResponse;
import com.hacknest.backend.dto.application.ApplicationSummaryResponse;
import com.hacknest.backend.dto.application.ApplyRequest;
import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.enums.ApplicationStatus;

public interface ApplicationService {
    ApplicationResponse applyToTeam(String teamId, String userId, ApplyRequest request);
    PagedResponse<ApplicationSummaryResponse> getTeamApplications(String teamId, String userId, ApplicationStatus status, int page, int size, String sortBy, String sortDirection);
    ApplicationResponse acceptApplication(String applicationId, String userId);
    ApplicationResponse rejectApplication(String applicationId, String userId);
    void withdrawApplication(String applicationId, String userId);
}
