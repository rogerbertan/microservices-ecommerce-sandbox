package dev.bertan.order_service.dto.notification;

import jakarta.validation.constraints.NotBlank;

public record CreateNotificationRequest(
    @NotBlank String message,
    @NotBlank String type,
    @NotBlank String recipient
) {}
