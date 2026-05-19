package dev.bertan.product_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private Integer minThreshold;

    private LocalDateTime createdAt;

    protected Product() {}

    private Product(String name, String description, BigDecimal price, Integer quantity, Integer minThreshold) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public static Product create(String name, String description, BigDecimal price, Integer quantity, Integer minThreshold) {
        return new Product(name, description, price, quantity, minThreshold);
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void consumeQuantity(@Positive Integer quantity) {
        this.quantity -= quantity;
    }

    public void addQuantity(@Positive Integer quantity) {
        this.quantity += quantity;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public BigDecimal getPrice() { return price; }

    public Integer getQuantity() { return quantity; }

    public Integer getMinThreshold() { return minThreshold; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
