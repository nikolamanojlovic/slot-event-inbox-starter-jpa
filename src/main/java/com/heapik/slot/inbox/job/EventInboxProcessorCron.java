package com.heapik.slot.inbox.job;

import com.heapik.slot.commonsevent.domain.inbox.EventInbox;
import com.heapik.slot.commonsevent.ports.publisher.EventPublisherPort;
import com.heapik.slot.inbox.autoconfig.EventInboxProperties;
import com.heapik.slot.inbox.service.EventInboxProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;

public class EventInboxProcessorCron {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventInboxProcessorCron.class);

    private final EventInboxProperties properties;

    private final EventInboxProcessorService eventInboxProcessorService;

    public EventInboxProcessorCron(EventInboxProperties properties, EventInboxProcessorService eventInboxProcessorService, EventPublisherPort eventPublisherPort) {
        this.properties = properties;
        this.eventInboxProcessorService = eventInboxProcessorService;
    }

    @Scheduled(cron = "${slot.event.inbox.scheduling-cron-expression}")
    public void processInboxEvents() {

        while (true) {
            var pagination = eventInboxProcessorService.getCursor();
            var batch = eventInboxProcessorService.getBatch(pagination, properties.getBatchSize());

            if (batch.isEmpty()) {
                LOGGER.info("No unpublished events found. Finished processing inbox events.");
                return;
            }

            var success = new ArrayList<EventInbox>();
            LOGGER.info("Processing {} events for cursor {}, {}", batch.size(), pagination.getCursor(), pagination.getTieBreaker());
            for (var event : batch) {
                try {
                    eventInboxProcessorService.processInboxEvent(event);
                    // TODO: [TechDept] Rename to processed in future
                    event.published();
                    success.add(event);
                    LOGGER.debug("Succeeded to process event {}, inbox id: {}",
                            event.getEventType(),
                            event.getId().toString());
                } catch (Exception e) {
                    LOGGER.error("Failed to process event {}, retry no {}, inbox id {}",
                            event.getEventType(),
                            event.getRetryCount() + 1,
                            event.getId().toString(),
                            e);
                    event.incrementRetryCount();
                    event.setErrorMessage(e.getMessage());
                }
            }

            eventInboxProcessorService.updateBatch(batch);

            if (!success.isEmpty()) {
                LOGGER.debug("Updating last successful cursor to {} {}", success.getLast().getId(), success.getLast().getOccurredAt());
                eventInboxProcessorService.updateLastSuccessfulCursor(pagination, success.getLast());
            }

            if (batch.size() < properties.getBatchSize()) {
                LOGGER.info("Finished processing outbox events.");
                return;
            }
        }
    }
}
