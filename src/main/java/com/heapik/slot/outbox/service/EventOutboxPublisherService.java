package com.heapik.slot.outbox.service;

import com.heapik.slot.commonsevent.domain.outbox.EventOutbox;
import com.heapik.slot.commonsevent.domain.outbox.EventOutboxCursor;
import com.heapik.slot.commonsevent.ports.outbox.EventOutboxCursorRepositoryPort;
import com.heapik.slot.commonsevent.ports.outbox.EventOutboxRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class EventOutboxPublisherService {

    // TODO: [TechDept] Move to configuration later
    private static final String PROCESSOR_NAME = "event-outbox-publisher";

    // TODO: [TechDept] Move to configuration later
    private static final int MAX_RETRIES = 3;

    private final EventOutboxRepositoryPort eventOutboxRepositoryPort;
    private final EventOutboxCursorRepositoryPort eventOutboxCursorRepositoryPort;

    public EventOutboxPublisherService(EventOutboxRepositoryPort eventOutboxRepositoryPort, EventOutboxCursorRepositoryPort eventOutboxCursorRepositoryPort) {
        this.eventOutboxRepositoryPort = eventOutboxRepositoryPort;
        this.eventOutboxCursorRepositoryPort = eventOutboxCursorRepositoryPort;
    }

    public EventOutboxCursor getCursor() {
        return eventOutboxCursorRepositoryPort.findByName(PROCESSOR_NAME)
                .orElseGet(() ->
                        eventOutboxCursorRepositoryPort.save(new EventOutboxCursor(
                                PROCESSOR_NAME,
                                Instant.EPOCH,
                                new UUID(0L, 0L)
                        )));
    }

    public List<EventOutbox> getBatch(EventOutboxCursor pagination, int batchSize) {
        return eventOutboxRepositoryPort.findAllUnpublishedEvents(
                pagination.getCursor(),
                pagination.getTieBreaker(),
                batchSize,
                MAX_RETRIES
        );
    }

    @Transactional
    public void updateBatchAndCursor(List<EventOutbox> batch, EventOutboxCursor cursor) {
        eventOutboxRepositoryPort.save(batch);

        cursor.setCursor(batch.getLast().getOccurredAt());
        cursor.setTieBreaker(batch.getLast().getId());
        eventOutboxCursorRepositoryPort.save(cursor);

    }
}
