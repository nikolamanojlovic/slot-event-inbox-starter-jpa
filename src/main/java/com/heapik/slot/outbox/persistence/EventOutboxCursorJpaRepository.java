package com.heapik.slot.outbox.persistence;

import com.heapik.slot.outbox.domain.EventOutboxCursorOrm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventOutboxCursorJpaRepository extends JpaRepository<EventOutboxCursorOrm, String> {

}
