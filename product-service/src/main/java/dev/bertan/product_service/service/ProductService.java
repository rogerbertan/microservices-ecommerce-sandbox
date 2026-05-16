package dev.bertan.product_service.service;

import dev.bertan.product_service.dto.CreateProductRequest;
import dev.bertan.product_service.entity.Product;
import dev.bertan.product_service.repository.ProductRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product create(CreateProductRequest req) {
        return repository.save(Product.create(req.name(), req.description(), req.price(), req.quantity()));
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Product consume(Long id, Integer quantity) {
        Product product = findById(id);
        product.consumeQuantity(quantity);
        return repository.save(product);
    }

    public Product add(Long id, Integer quantity) {
        Product product = findById(id);
        product.addQuantity(quantity);
        return repository.save(product);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }
}
