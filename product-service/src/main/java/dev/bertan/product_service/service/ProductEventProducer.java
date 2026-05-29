package dev.bertan.product_service.service;

import dev.bertan.product_service.dto.product.LowStockEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
public class ProductEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ProductEventProducer(KafkaTemplate<String, String> kafkaTemplate,
                                ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendProductLowStock(LowStockEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            String productId = String.valueOf(event.productId());
            kafkaTemplate.send("low.stock", productId, payload);
        } catch (JacksonException e) {
            throw new RuntimeException("Failed to serialize LowStockEvent", e);
        }
    }
}
