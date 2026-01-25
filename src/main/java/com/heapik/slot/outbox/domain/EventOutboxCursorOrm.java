package com.heapik.slot.outbox.domain;

import com.heapik.slot.commonsevent.domain.outbox.EventOutboxCursor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "event_outbox_cursor")
public class EventOutboxCursorOrm {

    @Id
    @Column(name = "processor_name", nullable = false)
    private String processorName;

    @Column(name = "cursor", nullable = false)
    private Instant cursor;

    @Column(name = "tie_breaker", nullable = false)
    private UUID tieBreaker;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public EventOutboxCursorOrm() {
    }

    private EventOutboxCursorOrm(String processorName, Instant cursor, UUID tieBreaker) {
        this.processorName = processorName;
        this.cursor = cursor;
        this.tieBreaker = tieBreaker;
    }

    public static EventOutboxCursorOrm fromDomain(EventOutboxCursor eventOutboxCursor) {
        return new EventOutboxCursorOrm(
                eventOutboxCursor.getProcessorName(),
                eventOutboxCursor.getCursor(),
                eventOutboxCursor.getTieBreaker()
        );
    }

    public EventOutboxCursor toDomain() {
        return new EventOutboxCursor(
                this.processorName,
                this.cursor,
                this.tieBreaker
        );
    }

    public String getProcessorName() {
        return processorName;
    }

    public Instant getCursor() {
        return cursor;
    }

    public UUID getTieBreaker() {
        return tieBreaker;
    }
}
