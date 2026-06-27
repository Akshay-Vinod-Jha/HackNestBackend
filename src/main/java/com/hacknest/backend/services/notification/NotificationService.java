package com.hacknest.backend.services.notification;

import com.hacknest.backend.dto.notification.CreateNotificationRequest;
import com.hacknest.backend.dto.notification.NotificationDto;
import com.hacknest.backend.models.User;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getUserNotifications(User user);
    long getUnreadCount(User user);
    NotificationDto createNotification(CreateNotificationRequest request);
    void markAsRead(String id, User user);
    void markAllAsRead(User user);
}
