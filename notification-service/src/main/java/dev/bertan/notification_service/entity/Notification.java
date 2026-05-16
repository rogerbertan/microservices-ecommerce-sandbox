package dev.bertan.notification_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String message;

    @NotBlank
    private String type;

    @NotBlank
    private String recipient;

    private LocalDateTime sentAt;

    protected Notification() {}

    private Notification(String message, String type, String recipient) {
        this.message = message;
        this.type = type;
        this.recipient = recipient;
    }

    public static Notification create(String message, String type, String recipient) {
        return new Notification(message, type, recipient);
    }

    public void update(String message, String type, String recipient) {
        this.message = message;
        this.type = type;
        this.recipient = recipient;
    }

    @PrePersist
    void onCreate() {
        this.sentAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public String getMessage() { return message; }

    public String getType() { return type; }

    public String getRecipient() { return recipient; }

    public LocalDateTime getSentAt() { return sentAt; }
}
