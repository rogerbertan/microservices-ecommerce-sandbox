package dev.bertan.notification_service.dto;

import dev.bertan.notification_service.entity.Notification;
import java.time.LocalDateTime;

public record NotificationResponse(
    Long id,
    String message,
    String type,
    String recipient,
    LocalDateTime sentAt
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
            notification.getId(),
            notification.getMessage(),
            notification.getType(),
            notification.getRecipient(),
            notification.getSentAt()
        );
    }
}
