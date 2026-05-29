package dev.bertan.notification_service.dto.product;

public record LowStockEvent(
        String eventId,
        Long productId,
        String name,
        Integer quantity,
        Integer minThreshold
) {}
