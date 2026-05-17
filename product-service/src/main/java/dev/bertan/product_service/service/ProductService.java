package dev.bertan.product_service.service;

import dev.bertan.product_service.dto.CreateProductRequest;
import dev.bertan.product_service.dto.ProductResponse;
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

    public ProductResponse create(CreateProductRequest req) {
        return ProductResponse.from(repository.save(Product.create(req.name(), req.description(), req.price(), req.quantity())));
    }

    public List<ProductResponse> findAll() {
        return repository.findAll().stream().map(ProductResponse::from).toList();
    }

    public ProductResponse findById(Long id) {
        return ProductResponse.from(findProductById(id));
    }

    public ProductResponse consume(Long id, Integer quantity) {
        Product product = findProductById(id);
        product.consumeQuantity(quantity);
        return ProductResponse.from(repository.save(product));
    }

    public ProductResponse add(Long id, Integer quantity) {
        Product product = findProductById(id);
        product.addQuantity(quantity);
        return ProductResponse.from(repository.save(product));
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
