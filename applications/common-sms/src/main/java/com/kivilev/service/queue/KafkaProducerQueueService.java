package com.kivilev.service.queue;

import com.kivilev.config.KafkaConfig;
import com.kivilev.model.Sms;
import com.kivilev.service.queue.mapper.SmsMessageMapper;
import com.kivilev.service.queue.model.SmsSendMessageDto;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@EnableConfigurationProperties(KafkaConfig.class)
public class KafkaProducerQueueService implements ProducerQueueSmsService {

    private final KafkaTemplate<String, SmsSendMessageDto> kafkaSendSmsTemplate;
    private final SmsMessageMapper smsMessageMapper;
    private final KafkaConfig kafkaConfig;

    public KafkaProducerQueueService(KafkaTemplate<String, SmsSendMessageDto> kafkaSendSmsTemplate, SmsMessageMapper smsMessageMapper, KafkaConfig kafkaConfig) {
        this.kafkaSendSmsTemplate = kafkaSendSmsTemplate;
        this.smsMessageMapper = smsMessageMapper;
        this.kafkaConfig = kafkaConfig;

    }

    @Override
    public void sendNewSmsMessages(List<Sms> smsList) {
        var smsSendMessageDtos = smsList.stream()
                .map(smsMessageMapper::toSmsSendMessageDto)
                .collect(Collectors.toMap(this::getMessageKey, Function.identity()));

        smsSendMessageDtos.forEach(
                (messageKey, smsStatusChangeMessageDto) ->
                        kafkaSendSmsTemplate.send(kafkaConfig.getSmsSendTopicName(), messageKey, smsStatusChangeMessageDto)
        );
    }

    private String getMessageKey(SmsSendMessageDto smsSendMessageDto) {
        return String.format("%s-%s", smsSendMessageDto.sourceId(), smsSendMessageDto.smsId());
    }
}
