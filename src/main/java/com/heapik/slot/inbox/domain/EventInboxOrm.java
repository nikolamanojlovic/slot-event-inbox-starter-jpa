package com.heapik.slot.inbox.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.heapik.slot.commonsevent.domain.inbox.EventInbox;
import com.heapik.slot.commonsevent.domain.outbox.EventOutbox;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "event_inbox",
        indexes = {
                @Index(name = "idx_event_inbox_unpublished", columnList = "occurred_at, id")
        }
)
public class EventInboxOrm {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "jsonb", nullable = false)
    private JsonNode payload;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "error_message")
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public EventInboxOrm() {
    }

    public EventInboxOrm(String eventType, JsonNode payload, Instant occurredAt) {
        this.eventType = eventType;
        this.payload = payload;
        this.occurredAt = occurredAt;
        this.published = false;
        this.retryCount = 0;
        this.errorMessage = null;
    }

    private EventInboxOrm(UUID id, String eventType, JsonNode payload, Instant occurredAt, boolean published, int retryCount, String errorMessage) {
        this.id = id;
        this.eventType = eventType;
        this.payload = payload;
        this.occurredAt = occurredAt;
        this.published = published;
        this.retryCount = retryCount;
        this.errorMessage = errorMessage;
    }

    public static EventInboxOrm fromDomain(EventInbox eventInbox) {
        return new EventInboxOrm(
                eventInbox.getId(),
                eventInbox.getEventType(),
                eventInbox.getPayload(),
                eventInbox.getOccurredAt(),
                eventInbox.isPublished(),
                eventInbox.getRetryCount(),
                eventInbox.getErrorMessage()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public boolean isPublished() {
        return published;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
