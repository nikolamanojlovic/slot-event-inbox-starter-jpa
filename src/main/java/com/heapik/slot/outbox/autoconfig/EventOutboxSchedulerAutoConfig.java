package com.heapik.slot.outbox.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty( prefix = "slot.event.outbox", name = "scheduling-enabled", havingValue = "true", matchIfMissing = true )
public class EventOutboxSchedulerAutoConfig {
}
