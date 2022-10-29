package com.kivilev.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;

@ConfigurationProperties(prefix = "kafka")
@ConstructorBinding
public class KafkaConfig {
    private static final String smsSendTopicParameterName = "sms-send-topic-name";
    private static final String smsStatusNotificationTopicParameterName = "sms-send-result-topic-name";

    private final String bootstrapServers;
    private final Map<String, String> producer;
    private final Map<String, String> consumer;
    private final Map<String, String> topics;

    public KafkaConfig(String bootstrapServers, Map<String, String> producer, Map<String, String> consumer, Map<String, String> topics) {
        this.bootstrapServers = bootstrapServers;
        this.producer = producer;
        this.consumer = consumer;
        this.topics = topics;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getProducerId() {
        return producer.get("id");
    }

    public String getSmsSendTopicName() {
        return topics.get(smsSendTopicParameterName);
    }

    public String getSmsSendResultTopicName() {
        return topics.get(smsStatusNotificationTopicParameterName);
    }

    public String getConsumerGroupId() {
        return consumer.get("group-id");
    }
}
