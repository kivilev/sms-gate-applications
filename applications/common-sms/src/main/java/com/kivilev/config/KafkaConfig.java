package com.kivilev.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
public class KafkaConfig {
    private String bootstrapServers;
    private String producerId;
    private String topicsSmsSendTopicNotificationQueueActualName;
    private String topicsSmsStatusTopicNotificationQueueActualName;
    private String consumerGroupId;

    public KafkaConfig() {
        // TODO: переделать на нормальное считывание из конфига
        producerId = "sms-send-producer";
        topicsSmsSendTopicNotificationQueueActualName = "sms-send-topic";
        topicsSmsStatusTopicNotificationQueueActualName = "sms-status-topic";
        consumerGroupId = "common-sms-sms-status-consumer-group";
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getTopicsSmsSendTopicNotificationQueueActualName() {
        return topicsSmsSendTopicNotificationQueueActualName;
    }

    public void setTopicsSmsSendTopicNotificationQueueActualName(String topicsSmsSendTopicNotificationQueueActualName) {
        this.topicsSmsSendTopicNotificationQueueActualName = topicsSmsSendTopicNotificationQueueActualName;
    }

    public String getTopicsSmsStatusTopicNotificationQueueActualName() {
        return topicsSmsStatusTopicNotificationQueueActualName;
    }

    public void setTopicsSmsStatusTopicNotificationQueueActualName(String topicsSmsStatusTopicNotificationQueueActualName) {
        this.topicsSmsStatusTopicNotificationQueueActualName = topicsSmsStatusTopicNotificationQueueActualName;
    }

    public String getConsumerGroupId() {
        return consumerGroupId;
    }

    public void setConsumerGroupId(String consumerGroupId) {
        this.consumerGroupId = consumerGroupId;
    }
}
