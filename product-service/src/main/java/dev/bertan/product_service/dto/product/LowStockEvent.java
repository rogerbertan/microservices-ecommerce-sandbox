package dev.bertan.product_service.dto.product;

import dev.bertan.product_service.entity.Product;

import java.util.UUID;

public record LowStockEvent(
        String eventId,
        Long productId,
        String name,
        Integer quantity,
        Integer minThreshold
) {
    public static LowStockEvent from(Product product, Integer quantity) {
        return new LowStockEvent(
                UUID.randomUUID().toString(),
                product.getId(),
                product.getName(),
                product.getQuantity() - quantity,
                product.getMinThreshold()
        );
    }
}
