package com.hacknest.backend.dto.hackathon;

import com.hacknest.backend.enums.HackathonMode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateHackathonRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Organizer is required")
    private String organizer;

    @URL(message = "Website URL must be valid")
    private String websiteUrl;

    @URL(message = "Registration URL must be valid")
    private String registrationUrl;

    @NotNull(message = "Mode is required")
    private HackathonMode mode;

    @NotNull(message = "Minimum team size is required")
    @Min(1)
    private Integer teamSizeMin;

    @NotNull(message = "Maximum team size is required")
    @Min(1)
    private Integer teamSizeMax;

    @NotNull(message = "Registration deadline is required")
    private LocalDateTime registrationDeadline;

    @NotNull(message = "Start date is required")
    private LocalDateTime hackathonStartDate;

    @NotNull(message = "End date is required")
    private LocalDateTime hackathonEndDate;

    private String prizePool;
    private String country;
    private String city;
    private List<String> domains;
    private List<String> techStacks;
    private List<String> tags;
}
