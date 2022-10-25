package com.kivilev.service.queue;

import com.kivilev.config.KafkaConfig;
import com.kivilev.service.SmsServiceImpl;
import com.kivilev.service.queue.mapper.SmsMessageMapper;
import com.kivilev.service.queue.model.SmsResultMessageDto;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableConfigurationProperties(KafkaConfig.class)
public class KafkaConsumerQueueService implements ConsumerQueueService {

    private final KafkaConfig kafkaConfig;
    private final SmsServiceImpl smsService;
    private final SmsMessageMapper smsMessageMapper;

    public KafkaConsumerQueueService(KafkaConfig kafkaConfig,
                                     SmsServiceImpl smsService,
                                     SmsMessageMapper smsMessageMapper) {
        this.kafkaConfig = kafkaConfig;
        this.smsService = smsService;
        this.smsMessageMapper = smsMessageMapper;
    }

    // TODO: брать из конфига
    @KafkaListener(id = "sms-result", topics = {"sms-result-topic"}, containerFactory = "batchFactory")
    @Override
    public void readSmsStatusMessages(List<SmsResultMessageDto> smsResultMessageDtos) {
        var smsStatusChangeMessages = smsResultMessageDtos.stream().map(smsMessageMapper::toSmsResultChange).toList();
        smsService.processSmsStatusMessages(smsStatusChangeMessages);
    }
}
