package dev.bertan.notification_service.service;

import dev.bertan.notification_service.dto.product.LowStockEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
public class ProductEventConsumer {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public ProductEventConsumer(NotificationService notificationService,
                                ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "low.stock", groupId = "notification-service-group")
    public void onLowStock(String payload) {
        try {
            LowStockEvent event = objectMapper.readValue(payload, LowStockEvent.class);
            notificationService.notifyLowStock(event);
        } catch (JacksonException e) {
            throw new RuntimeException("Failed to parse low stock payload", e);
        }
    }
}
