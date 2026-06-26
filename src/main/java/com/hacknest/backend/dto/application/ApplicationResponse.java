package com.hacknest.backend.dto.application;

import com.hacknest.backend.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationResponse {
    private String id;
    private String teamId;
    private String applicantId;
    private String roleApplied;
    private String message;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
