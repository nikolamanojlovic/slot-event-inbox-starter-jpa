package com.heapik.slot.inbox.persistence;

import com.heapik.slot.commonsevent.domain.inbox.EventInbox;
import com.heapik.slot.inbox.domain.EventInboxOrm;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EventInboxJpaRepository extends JpaRepository<EventInboxOrm, UUID> {

    @Query("""
                SELECT new com.heapik.slot.commonsevent.domain.inbox.EventInbox(
                    e.id, e.eventType, e.payload, e.occurredAt,
                    e.published, e.retryCount, e.errorMessage
                )
                FROM EventInboxOrm e
                WHERE e.published = false
                  AND e.retryCount < :retryCount
                  AND (
                        e.retryCount > 0
                        OR (
                            e.retryCount = 0
                            AND (
                                e.occurredAt > :cursor
                             OR (e.occurredAt = :cursor AND e.id > :tieBreaker)
                            )
                        )
                  )
                ORDER BY
                  e.retryCount DESC,
                  e.occurredAt ASC,
                  e.id ASC
            """)
    List<EventInbox> findUnpublishedEventOutboxesByRetryCountLessThan(
            @Param("retryCount") int retryCount,
            @Param("cursor") Instant cursor,
            @Param("tieBreaker") UUID tieBreaker,
            Pageable pageable);
}
