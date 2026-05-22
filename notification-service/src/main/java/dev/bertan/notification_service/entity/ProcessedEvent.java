package dev.bertan.notification_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "processed_events")
public class ProcessedEvent implements Persistable<UUID> {

    @Id
    private UUID eventId;

    @Transient
    private boolean isNew = true;

    private LocalDateTime processedAt;

    public ProcessedEvent() {}

    public ProcessedEvent(UUID eventId) {
        this.eventId = eventId;
    }

    @PrePersist
    public void prePersist() {
        processedAt = LocalDateTime.now();
        isNew = false;
    }

    @Override
    public @Nullable UUID getId() {
        return eventId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
}
