package com.hacknest.backend.models.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Experience {
    private String title;
    private String organization;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    
    @Builder.Default
    private boolean currentlyWorking = false;
}
