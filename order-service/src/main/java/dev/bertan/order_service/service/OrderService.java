package dev.bertan.order_service.service;

import dev.bertan.order_service.client.ProductClient;
import dev.bertan.order_service.dto.order.CreateOrderRequest;
import dev.bertan.order_service.dto.order.OrderCreatedEvent;
import dev.bertan.order_service.dto.order.OrderResponse;
import dev.bertan.order_service.dto.order.UpdateOrderRequest;
import dev.bertan.order_service.entity.Order;
import dev.bertan.order_service.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductClient productClient;
    private final OrderEventProducer orderEventProducer;

    public OrderService(OrderRepository repository,
                        ProductClient productClient,
                        OrderEventProducer orderEventProducer) {
        this.repository = repository;
        this.productClient = productClient;
        this.orderEventProducer = orderEventProducer;
    }

    @CircuitBreaker(name = "orderService", fallbackMethod = "createOrderFallback")
    @Retry(name = "orderService")
    public OrderResponse create(CreateOrderRequest req) {
        BigDecimal totalAmount = calculateTotalAmount(req.productQuantity(), req.productId());
        Order order = Order.create(req.customerName(), totalAmount, req.status());
        productClient.consume(req.productId(), req.productQuantity());
        Order savedOrder = repository.save(order);
        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.from(savedOrder);
        orderEventProducer.sendOrderCreated(orderCreatedEvent);

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> findAll() {
        return repository.findAll().stream()
                .map(OrderResponse::from)
                .toList();
    }

    public OrderResponse findById(Long id) {
        return OrderResponse.from(findOrderById(id));
    }

    public OrderResponse update(Long id, UpdateOrderRequest req) {
        Order existing = findOrderById(id);
        existing.update(req.customerName(), req.totalAmount(), req.status());
        return OrderResponse.from(repository.save(existing));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    private Order findOrderById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private BigDecimal calculateTotalAmount(Integer quantity, Long productId) {
        BigDecimal productPrice = productClient.getPrice(productId);
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private OrderResponse createOrderFallback() {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Product service is currently unavailable. Please try again later.");
    }
}
