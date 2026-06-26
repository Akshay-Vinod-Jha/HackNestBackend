package com.hacknest.backend.dto.invitation;

import com.hacknest.backend.enums.InvitationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InvitationResponse {
    private String id;
    private String teamId;
    private String senderId;
    private String receiverId;
    private String roleOffered;
    private String message;
    private InvitationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
