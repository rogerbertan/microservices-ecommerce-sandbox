package dev.bertan.order_service.service;

import dev.bertan.order_service.dto.CreateOrderRequest;
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

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public Order create(CreateOrderRequest req) {
        return repository.save(Order.create(req.customerName(), req.totalAmount(), req.status()));
    }

    public List<Order> findAll() {
        return repository.findAll();
    }

    public Order findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Order update(Long id, UpdateOrderRequest req) {
        Order existing = findById(id);
        existing.update(req.customerName(), req.totalAmount(), req.status());
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }
}
