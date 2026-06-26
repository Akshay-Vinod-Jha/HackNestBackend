package com.hacknest.backend.dto.invitation;

import com.hacknest.backend.enums.InvitationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InvitationSummaryResponse {
    private String id;
    private String roleOffered;
    private InvitationStatus status;
    private LocalDateTime createdAt;
    
    private String teamId;
    private String teamName;
    
    private String senderId;
    private String senderName;
    
    private String receiverId;
    private String receiverName;
}
