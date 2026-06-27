package com.hacknest.backend.dto.notification;

import com.hacknest.backend.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private String id;
    private String recipientId;
    private NotificationType type;
    private String title;
    private String message;
    private String relatedEntityId;
    private boolean isRead;
    private LocalDateTime createdAt;
}
