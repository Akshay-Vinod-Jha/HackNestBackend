package com.hacknest.backend.dto.application;

import com.hacknest.backend.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationSummaryResponse {
    private String id;
    private String roleApplied;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private String applicantId;
    private String applicantName;
}
