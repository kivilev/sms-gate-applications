package com.kivilev.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

@Configuration
@EnableScheduling
@EnableKafka
public class ApplicationConfig {
    @Bean
    ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    Clock getClock() {
        return Clock.systemUTC();
    }
}
