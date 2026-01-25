package com.heapik.slot.outbox.autoconfig;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EventOutboxProperties.class)
public class EvenOutboxAutoConfig {
}
