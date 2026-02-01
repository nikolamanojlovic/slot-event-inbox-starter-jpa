package com.heapik.slot.inbox.autoconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.heapik.slot.commonsevent.ports.inbox.EventInboxCursorRepositoryPort;
import com.heapik.slot.commonsevent.ports.inbox.EventInboxRepositoryPort;
import com.heapik.slot.commonsevent.ports.outbox.EventOutboxRepositoryPort;
import com.heapik.slot.inbox.persistence.EventInboxCursorJpaRepository;
import com.heapik.slot.inbox.persistence.EventInboxCursorRepositoryAdapter;
import com.heapik.slot.inbox.persistence.EventInboxJpaRepository;
import com.heapik.slot.inbox.persistence.EventInboxRepositoryAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableConfigurationProperties(EventInboxProperties.class)
@EnableJpaRepositories(basePackages = "com.heapik.slot.inbox.persistence")
@EntityScan(basePackages = "com.heapik.slot.inbox.domain")
public class EvenInboxAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        var om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return om;
    }

    @Bean
    @ConditionalOnMissingBean(EventInboxCursorRepositoryPort.class)
    public EventInboxCursorRepositoryPort eventInboxCursorRepository(
            EventInboxCursorJpaRepository eventInboxCursorJpaRepository
    ) {
        return new EventInboxCursorRepositoryAdapter(eventInboxCursorJpaRepository);
    }

    @Bean
    @ConditionalOnMissingBean(EventOutboxRepositoryPort.class)
    public EventInboxRepositoryPort eventInboxRepository(
            EventInboxJpaRepository eventInboxJpaRepository,
            ObjectMapper objectMapper
    ) {
        return new EventInboxRepositoryAdapter(eventInboxJpaRepository, objectMapper);
    }
}
