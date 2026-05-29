package dev.bertan.product_service.service;

import dev.bertan.product_service.dto.product.CreateProductRequest;
import dev.bertan.product_service.dto.product.LowStockEvent;
import dev.bertan.product_service.dto.product.ProductResponse;
import dev.bertan.product_service.entity.Product;
import dev.bertan.product_service.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductEventProducer productEventProducer;

    public ProductService(ProductRepository repository,
                          ProductEventProducer productEventProducer) {
        this.repository = repository;
        this.productEventProducer = productEventProducer;
    }

    public ProductResponse create(CreateProductRequest req) {
        return ProductResponse.from(repository.save(Product.create(req.name(), req.description(), req.price(), req.quantity(), req.minThreshold())));
    }

    public List<ProductResponse> findAll() {
        return repository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    public ProductResponse findById(Long id) {
        return ProductResponse.from(findProductById(id));
    }

    public void consume(Long id, Integer quantity) {
        Product product = findProductById(id);
        if (quantity > product.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough quantity available");
        }
        if (product.getQuantity() - quantity < product.getMinThreshold()) {
            LowStockEvent request = LowStockEvent.from(product, quantity);
            productEventProducer.sendProductLowStock(request);
        }
        product.consumeQuantity(quantity);
        ProductResponse.from(repository.save(product));
    }

    public ProductResponse add(Long id, Integer quantity) {
        Product product = findProductById(id);
        product.addQuantity(quantity);
        return ProductResponse.from(repository.save(product));
    }

    public BigDecimal getPrice(Long id) {
        Product product = findProductById(id);
        return product.getPrice();
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    private Product findProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
