package com.heapik.slot.inbox.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heapik.slot.commonsevent.domain.Event;
import com.heapik.slot.commonsevent.domain.inbox.EventInbox;
import com.heapik.slot.commonsevent.ports.inbox.EventInboxRepositoryPort;
import com.heapik.slot.inbox.domain.EventInboxOrm;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class EventInboxRepositoryAdapter implements EventInboxRepositoryPort {

    private final EventInboxJpaRepository eventInboxJpaRepository;
    private final ObjectMapper objectMapper;

    public EventInboxRepositoryAdapter(EventInboxJpaRepository eventInboxJpaRepository, ObjectMapper objectMapper) {
        this.eventInboxJpaRepository = eventInboxJpaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<EventInbox> findAllUnpublishedEvents(Instant cursor, UUID tieBreaker, int limit, int retryCount) {
        return eventInboxJpaRepository.findUnpublishedEventOutboxesByRetryCountLessThan(retryCount, cursor, tieBreaker, Pageable.ofSize(limit));
    }

    @Override
    public void save(List<EventInbox> list) {
        var mapped = list.stream().map(EventInboxOrm::fromDomain).toList();
        eventInboxJpaRepository.saveAll(mapped);
    }

    @Override
    public void saveEvent(Event<?> event) {
        var outbox = new EventInboxOrm(event.eventType(), objectMapper.valueToTree(event.payload()), event.occurredAt());
        eventInboxJpaRepository.save(outbox);
    }
}
