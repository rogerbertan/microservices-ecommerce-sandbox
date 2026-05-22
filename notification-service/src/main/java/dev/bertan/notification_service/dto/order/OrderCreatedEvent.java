package dev.bertan.notification_service.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCreatedEvent(
        String eventId,
        Long id,
        String customerName,
        BigDecimal totalAmount,
        String status,
        LocalDateTime createdAt
) {}
