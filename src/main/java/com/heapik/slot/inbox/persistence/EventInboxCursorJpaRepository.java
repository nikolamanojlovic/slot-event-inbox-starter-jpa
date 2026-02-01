package com.heapik.slot.inbox.persistence;

import com.heapik.slot.inbox.domain.EventInboxCursorOrm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventInboxCursorJpaRepository extends JpaRepository<EventInboxCursorOrm, String> {

}
