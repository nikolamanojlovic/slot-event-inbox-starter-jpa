package com.heapik.slot.outbox.autoconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heapik.slot.commonsevent.ports.outbox.EventOutboxCursorRepositoryPort;
import com.heapik.slot.commonsevent.ports.outbox.EventOutboxRepositoryPort;
import com.heapik.slot.outbox.persistence.EventOutboxCursorJpaRepository;
import com.heapik.slot.outbox.persistence.EventOutboxCursorRepositoryAdapter;
import com.heapik.slot.outbox.persistence.EventOutboxJpaRepository;
import com.heapik.slot.outbox.persistence.EventOutboxRepositoryAdapter;
import com.heapik.slot.outbox.service.EventOutboxPublisherService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableConfigurationProperties(EventOutboxProperties.class)
@EnableJpaRepositories(basePackages = "com.heapik.slot.outbox.persistence")
@EntityScan(basePackages = "com.heapik.slot.outbox.domain")
public class EvenOutboxAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventOutboxPublisherService eventOutboxPublisherService(
            EventOutboxRepositoryPort repositoryPort,
            EventOutboxCursorRepositoryPort cursorRepositoryPort
    ) {
        return new EventOutboxPublisherService(repositoryPort, cursorRepositoryPort);
    }

    @Bean
    @ConditionalOnMissingBean(EventOutboxCursorRepositoryPort.class)
    public EventOutboxCursorRepositoryPort eventOutboxCursorRepository(
            EventOutboxCursorJpaRepository eventOutboxCursorJpaRepository
    ) {
        return new EventOutboxCursorRepositoryAdapter(eventOutboxCursorJpaRepository);
    }

    @Bean
    @ConditionalOnMissingBean(EventOutboxRepositoryPort.class)
    public EventOutboxRepositoryPort eventOutboxRepository(
            EventOutboxJpaRepository eventOutboxJpaRepository,
            ObjectMapper objectMapper
    ) {
        return new EventOutboxRepositoryAdapter(eventOutboxJpaRepository, objectMapper);
    }
}
