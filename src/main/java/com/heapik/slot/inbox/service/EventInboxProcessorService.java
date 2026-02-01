package com.heapik.slot.inbox.service;

import com.heapik.slot.commonsevent.domain.inbox.EventInbox;
import com.heapik.slot.commonsevent.domain.inbox.EventInboxCursor;
import com.heapik.slot.commonsevent.ports.inbox.EventInboxCursorRepositoryPort;
import com.heapik.slot.commonsevent.ports.inbox.EventInboxRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public abstract class EventInboxProcessorService {

    // TODO: [TechDept] Move to configuration later
    private static final String PROCESSOR_NAME = "event-inbox-processor";

    // TODO: [TechDept] Move to configuration later
    private static final int MAX_RETRIES = 3;

    private final EventInboxRepositoryPort eventInboxRepositoryPort;
    private final EventInboxCursorRepositoryPort eventInboxCursorRepositoryPort;

    public EventInboxProcessorService(EventInboxRepositoryPort eventInboxRepositoryPort, EventInboxCursorRepositoryPort eventInboxCursorRepositoryPort) {
        this.eventInboxRepositoryPort = eventInboxRepositoryPort;
        this.eventInboxCursorRepositoryPort = eventInboxCursorRepositoryPort;
    }

    public EventInboxCursor getCursor() {
        return eventInboxCursorRepositoryPort.findByName(PROCESSOR_NAME)
                .orElseGet(() ->
                        eventInboxCursorRepositoryPort.save(new EventInboxCursor(
                                PROCESSOR_NAME,
                                Instant.EPOCH,
                                new UUID(0L, 0L)
                        )));
    }

    public List<EventInbox> getBatch(EventInboxCursor pagination, int batchSize) {
        return eventInboxRepositoryPort.findAllUnpublishedEvents(
                pagination.getCursor(),
                pagination.getTieBreaker(),
                batchSize,
                MAX_RETRIES
        );
    }

    @Transactional
    public void updateBatch(List<EventInbox> batch) {
        eventInboxRepositoryPort.save(batch);
    }

    @Transactional
    public void updateLastSuccessfulCursor(EventInboxCursor cursor, EventInbox lastEvent) {
        cursor.setCursor(lastEvent.getOccurredAt());
        cursor.setTieBreaker(lastEvent.getId());
        eventInboxCursorRepositoryPort.save(cursor);
    }

    public abstract void processEventInbox(EventInbox eventInbox);
}
