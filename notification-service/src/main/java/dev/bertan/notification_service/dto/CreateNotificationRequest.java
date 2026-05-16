package dev.bertan.notification_service.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateNotificationRequest(
    @NotBlank String message,
    @NotBlank String type,
    @NotBlank String recipient
) {}
