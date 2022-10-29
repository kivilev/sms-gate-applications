package com.kivilev.service.queue;

import com.kivilev.config.KafkaConfig;
import com.kivilev.service.SmsService;
import com.kivilev.service.queue.mapper.SmsMessageMapper;
import com.kivilev.service.queue.model.SmsSendMessageDto;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableConfigurationProperties(KafkaConfig.class)
public class KafkaConsumerQueueService implements ConsumerQueueService {

    private final KafkaConfig kafkaConfig;
    private final SmsService smsService;
    private final SmsMessageMapper smsMessageMapper;

    public KafkaConsumerQueueService(KafkaConfig kafkaConfig,
                                     SmsService smsService,
                                     SmsMessageMapper smsMessageMapper) {
        this.kafkaConfig = kafkaConfig;
        this.smsService = smsService;
        this.smsMessageMapper = smsMessageMapper;
    }

    @KafkaListener(id = "sms-send", topics = {"${kafka.topics.sms-send-topic-name}"}, containerFactory = "batchFactory")
    @Override
    public void readSmsSendMessages(List<SmsSendMessageDto> smsSendMessageDtos) {
        var smsList = smsSendMessageDtos.stream().map(smsMessageMapper::toSms).toList();
        smsService.processNewSmsMessages(smsList);
    }
}
