package dev.bertan.product_service.controller;

import dev.bertan.product_service.dto.CreateProductRequest;
import dev.bertan.product_service.dto.ProductResponse;
import dev.bertan.product_service.service.ProductService;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @GetMapping
    public List<ProductResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PatchMapping("/{id}/consume")
    public void consume(@PathVariable Long id, @RequestBody Integer quantity) {
        service.consume(id, quantity);
    }

    @PatchMapping("/{id}/add")
    public ProductResponse add(@PathVariable Long id, @RequestBody Integer quantity) {
        return service.add(id, quantity);
    }

    @GetMapping("/{id}/price")
    public BigDecimal getPrice(@PathVariable Long id) {
        return service.getPrice(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
