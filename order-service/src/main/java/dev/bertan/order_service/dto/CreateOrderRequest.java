package dev.bertan.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
    @NotBlank String customerName,
    @NotBlank String status,
    @NotNull Long productId,
    @NotNull Integer productQuantity
) {}
