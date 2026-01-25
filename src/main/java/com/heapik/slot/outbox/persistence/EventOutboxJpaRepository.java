package com.heapik.slot.outbox.persistence;

import com.heapik.slot.commonsevent.domain.outbox.EventOutbox;
import com.heapik.slot.outbox.domain.EventOutboxOrm;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EventOutboxJpaRepository extends JpaRepository<EventOutboxOrm, UUID> {

    @Query("""
            SELECT new com.heapik.slot.commonsevent.domain.outbox.EventOutbox(e.id,e.eventType, e.payload, e.occurredAt, e.published, e.retryCount, e.errorMessage)
            FROM EventOutboxOrm e
            WHERE e.published = false AND e.retryCount < :retryCount
                AND (e.occurredAt > :cursor OR (e.occurredAt = :cursor AND e.id > :tieBreaker))
            ORDER BY e.occurredAt ASC, e.id ASC
            """)
    List<EventOutbox> findUnpublishedEventOutboxesByRetryCountLessThan(
            @Param("retryCount") int retryCount,
            @Param("cursor") Instant cursor,
            @Param("tieBreaker") UUID tieBreaker,
            Pageable pageable);
}
