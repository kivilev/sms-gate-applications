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

    private final SmsServiceImpl smsService;
    private final SmsMessageMapper smsMessageMapper;

    public KafkaConsumerQueueService(SmsServiceImpl smsService,
                                     SmsMessageMapper smsMessageMapper) {
        this.smsService = smsService;
        this.smsMessageMapper = smsMessageMapper;
    }

    @KafkaListener(id = "sms-result", topics = "${kafka.topics.sms-send-result-topic-name}", containerFactory = "batchFactory")
    @Override
    public void readSmsStatusMessages(List<SmsResultMessageDto> smsResultMessageDtos) {
        var smsStatusChangeMessages = smsResultMessageDtos.stream().map(smsMessageMapper::toSmsResultChange).toList();
        smsService.processSmsStatusMessages(smsStatusChangeMessages);
    }
}
