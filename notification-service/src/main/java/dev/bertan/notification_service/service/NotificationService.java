package dev.bertan.notification_service.service;

import dev.bertan.notification_service.dto.CreateNotificationRequest;
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

    public Notification create(CreateNotificationRequest req) {
        return repository.save(Notification.create(req.message(), req.type(), req.recipient()));
    }

    public List<Notification> findAll() {
        return repository.findAll();
    }

    public Notification findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Notification update(Long id, UpdateNotificationRequest req) {
        Notification existing = findById(id);
        existing.update(req.message(), req.type(), req.recipient());
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }
}
