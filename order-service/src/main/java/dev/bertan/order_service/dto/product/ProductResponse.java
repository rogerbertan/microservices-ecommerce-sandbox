package dev.bertan.order_service.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer quantity,
    LocalDateTime createdAt
) {}
