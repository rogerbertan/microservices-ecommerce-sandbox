package dev.bertan.notification_service.service;

import dev.bertan.notification_service.dto.CreateNotificationRequest;
import dev.bertan.notification_service.dto.NotificationResponse;
import dev.bertan.notification_service.dto.UpdateNotificationRequest;
import dev.bertan.notification_service.entity.Notification;
import dev.bertan.notification_service.repository.NotificationRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public NotificationResponse create(CreateNotificationRequest req) {
        return NotificationResponse.from(repository.save(Notification.create(req.message(), req.type(), req.recipient())));
    }

    public List<NotificationResponse> findAll() {
        return repository.findAll().stream().map(NotificationResponse::from).toList();
    }

    public NotificationResponse findById(Long id) {
        return NotificationResponse.from(findNotificationById(id));
    }

    public NotificationResponse update(Long id, UpdateNotificationRequest req) {
        Notification existing = findNotificationById(id);
        existing.update(req.message(), req.type(), req.recipient());
        return NotificationResponse.from(repository.save(existing));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    private Notification findNotificationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
