package com.hacknest.backend.controllers.notification;

import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.dto.notification.CreateNotificationRequest;
import com.hacknest.backend.dto.notification.NotificationDto;
import com.hacknest.backend.models.User;
import com.hacknest.backend.services.notification.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getUserNotifications(@AuthenticationPrincipal User user) {
        List<NotificationDto> notifications = notificationService.getUserNotifications(user);
        return ResponseEntity.ok(ApiResponse.success("Notifications fetched successfully", notifications));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@AuthenticationPrincipal User user) {
        long count = notificationService.getUnreadCount(user);
        return ResponseEntity.ok(ApiResponse.success("Unread count fetched successfully", count));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationDto>> createNotification(@Valid @RequestBody CreateNotificationRequest request) {
        NotificationDto notification = notificationService.createNotification(request);
        return new ResponseEntity<>(ApiResponse.success("Notification created", notification), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable String id, @AuthenticationPrincipal User user) {
        notificationService.markAsRead(id, user);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", null));
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(@AuthenticationPrincipal User user) {
        notificationService.markAllAsRead(user);
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read", null));
    }
}
