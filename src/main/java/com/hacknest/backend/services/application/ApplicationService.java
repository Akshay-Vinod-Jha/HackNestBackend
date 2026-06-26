package com.hacknest.backend.services.application;

import com.hacknest.backend.dto.application.ApplicationResponse;
import com.hacknest.backend.dto.application.ApplyRequest;

public interface ApplicationService {
    ApplicationResponse applyToTeam(String teamId, String userId, ApplyRequest request);
}
