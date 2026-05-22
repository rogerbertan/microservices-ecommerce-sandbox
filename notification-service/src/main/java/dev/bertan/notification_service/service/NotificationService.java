package dev.bertan.notification_service.service;

import dev.bertan.notification_service.dto.notification.CreateNotificationRequest;
import dev.bertan.notification_service.dto.notification.NotificationResponse;
import dev.bertan.notification_service.dto.order.OrderCreatedEvent;
import dev.bertan.notification_service.dto.notification.UpdateNotificationRequest;
import dev.bertan.notification_service.entity.Notification;
import dev.bertan.notification_service.entity.ProcessedEvent;
import dev.bertan.notification_service.repository.NotificationRepository;
import java.util.List;
import java.util.UUID;

import dev.bertan.notification_service.repository.ProcessedEventRepository;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class NotificationService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository repository;
    private final ProcessedEventRepository processedEventRepository;

    public NotificationService(NotificationRepository repository,
                               ProcessedEventRepository processedEventRepository) {
        this.repository = repository;
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
    public NotificationResponse create(CreateNotificationRequest req) {
        return NotificationResponse.from(repository.save(Notification.create(
                req.message(), req.type(), req.recipient())));
    }

    @Transactional
    public void notifyOrderCreated(OrderCreatedEvent req) {
        UUID eventId = UUID.fromString(req.eventId());
        try {
            processedEventRepository.save(new ProcessedEvent(eventId));
        } catch (DataIntegrityViolationException e) {
            log.info("Evento {} já processado, ignorando", eventId);
            return;
        }
        repository.save(Notification.create(
                "Pedido " + req.id() + " com status " + req.status() + " criado em " + req.createdAt(),
                "ORDER", req.customerName()));
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> findAll() {
        return repository.findAll().stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public NotificationResponse findById(Long id) {
        return NotificationResponse.from(findNotificationById(id));
    }

    @Transactional
    public NotificationResponse update(Long id, UpdateNotificationRequest req) {
        Notification existing = findNotificationById(id);
        existing.update(req.message(), req.type(), req.recipient());
        return NotificationResponse.from(repository.save(existing));
    }

    @Transactional
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
