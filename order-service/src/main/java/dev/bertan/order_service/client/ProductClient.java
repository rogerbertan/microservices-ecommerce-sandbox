package dev.bertan.order_service.client;

import dev.bertan.order_service.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "product-service", url = "http://localhost:8081")
public interface ProductClient {

    @GetMapping()
    ProductResponse consume(@PathVariable Long id, @RequestBody Integer quantity);
}
