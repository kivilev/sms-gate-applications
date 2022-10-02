package com.kivilev.config;

import com.kivilev.service.queue.model.SmsStatusChangeMessageDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(KafkaConfig.class)
public class KafkaProducerConfig {

    private final String kafkaServer;
    private final String kafkaProducerId;

    public KafkaProducerConfig(KafkaConfig kafkaConfig) {
        kafkaServer = kafkaConfig.getBootstrapServers();
        kafkaProducerId = kafkaConfig.getProducerId();
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerId);
        return props;
    }

    @Bean
    public ProducerFactory<String, SmsStatusChangeMessageDto> producerSmsSendFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, SmsStatusChangeMessageDto> kafkaTemplate() {
        KafkaTemplate<String, SmsStatusChangeMessageDto> template = new KafkaTemplate<>(producerSmsSendFactory());
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }
}