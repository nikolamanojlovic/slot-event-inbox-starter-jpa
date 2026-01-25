package com.heapik.slot.outbox.persistence;

import com.heapik.slot.commonsevent.domain.outbox.EventOutboxCursor;
import com.heapik.slot.commonsevent.ports.outbox.EventOutboxCursorRepositoryPort;
import com.heapik.slot.outbox.domain.EventOutboxCursorOrm;

import java.util.Optional;

public class EventOutboxCursorRepositoryAdapter implements EventOutboxCursorRepositoryPort {

    private final EventOutboxCursorJpaRepository eventOutboxCursorJpaRepository;

    public EventOutboxCursorRepositoryAdapter(EventOutboxCursorJpaRepository eventOutboxCursorJpaRepository) {
        this.eventOutboxCursorJpaRepository = eventOutboxCursorJpaRepository;
    }

    @Override
    public Optional<EventOutboxCursor> findByName(String name) {
        return eventOutboxCursorJpaRepository.findById(name).map(EventOutboxCursorOrm::toDomain);
    }

    @Override
    public EventOutboxCursor save(EventOutboxCursor eventOutboxCursor) {
        return eventOutboxCursorJpaRepository.save(EventOutboxCursorOrm.fromDomain(eventOutboxCursor)).toDomain();
    }
}
