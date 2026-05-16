package dev.bertan.order_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String customerName;

    @NotNull
    @Positive
    private BigDecimal totalAmount;

    @NotBlank
    private String status;

    private LocalDateTime createdAt;

    protected Order() {}

    private Order(String customerName, BigDecimal totalAmount, String status) {
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public static Order create(String customerName, BigDecimal totalAmount, String status) {
        return new Order(customerName, totalAmount, status);
    }

    public void update(String customerName, BigDecimal totalAmount, String status) {
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public String getCustomerName() { return customerName; }

    public BigDecimal getTotalAmount() { return totalAmount; }

    public String getStatus() { return status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
