package com.hacknest.backend.dto.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApplyRequest {
    @NotBlank(message = "Role applied cannot be empty")
    private String roleApplied;

    @Size(max = 500, message = "Message must not exceed 500 characters")
    private String message;
}
