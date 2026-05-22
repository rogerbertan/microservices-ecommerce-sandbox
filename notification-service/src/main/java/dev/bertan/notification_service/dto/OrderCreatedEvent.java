package dev.bertan.notification_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCreatedEvent(
        Long id,
        String customerName,
        BigDecimal totalAmount,
        String status,
        LocalDateTime createdAt
) {}
