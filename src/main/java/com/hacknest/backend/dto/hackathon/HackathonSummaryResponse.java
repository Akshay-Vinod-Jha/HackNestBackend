package com.hacknest.backend.dto.hackathon;

import com.hacknest.backend.enums.HackathonMode;
import com.hacknest.backend.enums.HackathonStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class HackathonSummaryResponse {
    private String id;
    private String title;
    private String organizer;
    private HackathonMode mode;
    private HackathonStatus status;
    private LocalDateTime registrationDeadline;
    private LocalDateTime hackathonStartDate;
    private String country;
    private String city;
    private List<String> tags;
}
