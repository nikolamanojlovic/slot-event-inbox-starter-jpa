package com.heapik.slot.inbox.persistence;

import com.heapik.slot.commonsevent.domain.inbox.EventInboxCursor;
import com.heapik.slot.commonsevent.ports.inbox.EventInboxCursorRepositoryPort;
import com.heapik.slot.inbox.domain.EventInboxCursorOrm;

import java.util.Optional;

public class EventInboxCursorRepositoryAdapter implements EventInboxCursorRepositoryPort {

    private final EventInboxCursorJpaRepository eventInboxCursorJpaRepository;

    public EventInboxCursorRepositoryAdapter(EventInboxCursorJpaRepository eventInboxCursorJpaRepository) {
        this.eventInboxCursorJpaRepository = eventInboxCursorJpaRepository;
    }

    @Override
    public Optional<EventInboxCursor> findByName(String name) {
        return eventInboxCursorJpaRepository.findById(name).map(EventInboxCursorOrm::toDomain);
    }

    @Override
    public EventInboxCursor save(EventInboxCursor eventOutboxCursor) {
        return eventInboxCursorJpaRepository.save(EventInboxCursorOrm.fromDomain(eventOutboxCursor)).toDomain();
    }
}
