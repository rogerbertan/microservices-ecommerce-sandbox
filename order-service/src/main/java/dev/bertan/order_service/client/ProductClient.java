package dev.bertan.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "product-service",
        url = "http://localhost:8081/products")
public interface ProductClient {

    @PatchMapping("/{id}/consume")
    void consume(@PathVariable Long id, @RequestBody Integer quantity);
}
