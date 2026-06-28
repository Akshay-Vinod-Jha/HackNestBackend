package com.hacknest.backend.dto.invitation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SendInvitationRequest {
    @NotBlank(message = "Receiver Email cannot be empty")
    private String receiverEmail;

    @NotBlank(message = "Role offered cannot be empty")
    private String roleOffered;

    @Size(max = 500, message = "Message must not exceed 500 characters")
    private String message;
}
