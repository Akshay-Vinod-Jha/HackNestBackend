package com.hacknest.backend.dto.profile;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ExperienceDto {
    private String title;
    private String organization;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean currentlyWorking;
}
