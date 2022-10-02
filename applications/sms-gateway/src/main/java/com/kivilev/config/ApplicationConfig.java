package com.kivilev.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableKafka
@EnableScheduling
public class ApplicationConfig {

    private final Integer packageSize = 10;

    @Bean
    ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }



}
