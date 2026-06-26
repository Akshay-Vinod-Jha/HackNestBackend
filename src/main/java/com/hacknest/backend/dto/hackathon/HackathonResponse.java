package com.hacknest.backend.dto.hackathon;

import com.hacknest.backend.enums.HackathonMode;
import com.hacknest.backend.enums.HackathonStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class HackathonResponse {
    private String id;
    private String title;
    private String description;
    private String organizer;
    private String websiteUrl;
    private String registrationUrl;
    private HackathonMode mode;
    private HackathonStatus status;
    private Integer teamSizeMin;
    private Integer teamSizeMax;
    private LocalDateTime registrationDeadline;
    private LocalDateTime hackathonStartDate;
    private LocalDateTime hackathonEndDate;
    private String prizePool;
    private String country;
    private String city;
    private List<String> domains;
    private List<String> techStacks;
    private List<String> tags;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
