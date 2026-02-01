package com.heapik.slot.inbox.domain;

import com.heapik.slot.commonsevent.domain.inbox.EventInboxCursor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "event_inbox_cursor")
public class EventInboxCursorOrm {

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

    public EventInboxCursorOrm() {
    }

    private EventInboxCursorOrm(String processorName, Instant cursor, UUID tieBreaker) {
        this.processorName = processorName;
        this.cursor = cursor;
        this.tieBreaker = tieBreaker;
    }

    public static EventInboxCursorOrm fromDomain(EventInboxCursor eventInboxCursor) {
        return new EventInboxCursorOrm(
                eventInboxCursor.getProcessorName(),
                eventInboxCursor.getCursor(),
                eventInboxCursor.getTieBreaker()
        );
    }

    public EventInboxCursor toDomain() {
        return new EventInboxCursor(
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
