package dev.bertan.order_service.service;

import dev.bertan.order_service.dto.order.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.JsonNodeException;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderEventProducer(KafkaTemplate<String, String> kafkaTemplate,
                              ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendOrderCreated(OrderCreatedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            String orderId = String.valueOf(event.id());
            kafkaTemplate.send("order.created", orderId, payload);
        } catch (JsonNodeException e) {
            throw new RuntimeException("Failed to serialize OrderCreatedEvent", e);
        }
    }
}
