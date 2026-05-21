package dev.bertan.order_service.dto.order;

import dev.bertan.order_service.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCreatedEvent(
        Long id,
        String customerName,
        BigDecimal totalAmount,
        String status,
        LocalDateTime createdAt
) {
    public static OrderCreatedEvent from(Order order) {
        return new OrderCreatedEvent(
                order.getId(),
                order.getCustomerName(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
