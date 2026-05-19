package dev.bertan.order_service.client;

import dev.bertan.order_service.dto.notification.CreateNotificationRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "notification-service",
        url = "http://localhost:8083/notifications")
public interface NotificationClient {

    @PostMapping
    void create(@Valid @RequestBody CreateNotificationRequest req);
}
