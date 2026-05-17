package dev.bertan.order_service.service;

import dev.bertan.order_service.client.ProductClient;
import dev.bertan.order_service.dto.CreateOrderRequest;
import dev.bertan.order_service.dto.OrderResponse;
import dev.bertan.order_service.dto.UpdateOrderRequest;
import dev.bertan.order_service.entity.Order;
import dev.bertan.order_service.repository.OrderRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductClient productClient;

    public OrderService(OrderRepository repository, ProductClient productClient) {
        this.repository = repository;
        this.productClient = productClient;
    }

    public OrderResponse create(CreateOrderRequest req) {
        return OrderResponse.from(repository.save(Order.create(req.customerName(), req.totalAmount(), req.status())));
    }

    public List<OrderResponse> findAll() {
        return repository.findAll().stream().map(OrderResponse::from).toList();
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
}
