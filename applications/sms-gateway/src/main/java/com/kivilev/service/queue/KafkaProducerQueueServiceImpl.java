package com.kivilev.service.queue;

import com.kivilev.config.KafkaConfig;
import com.kivilev.service.model.Sms;
import com.kivilev.service.queue.mapper.SmsMessageMapper;
import com.kivilev.service.queue.model.SmsResultMessageDto;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@EnableConfigurationProperties(KafkaConfig.class)
public class KafkaProducerQueueServiceImpl implements ProducerQueueService {
    private final KafkaTemplate<String, SmsResultMessageDto> kafkaSendSmsStatusTemplate;

    private final KafkaConfig kafkaConfig;
    private final SmsMessageMapper smsMessageMapper;

    public KafkaProducerQueueServiceImpl(KafkaTemplate<String, SmsResultMessageDto> kafkaSendSmsStatusTemplate,
                                         KafkaConfig kafkaConfig,
                                         SmsMessageMapper smsMessageMapper) {
        this.kafkaSendSmsStatusTemplate = kafkaSendSmsStatusTemplate;
        this.kafkaConfig = kafkaConfig;
        this.smsMessageMapper = smsMessageMapper;
    }

    @Override
    public void sendSmsStatusMessages(List<Sms> smsList) {
        var smsStatusMessageDtos = smsList.stream()
                .map(smsMessageMapper::toSmsStatusMessageDto)
                .collect(Collectors.toMap(KafkaProducerQueueServiceImpl::getMessageKey, Function.identity()));

        smsStatusMessageDtos.forEach(
                (messageKey, smsResultMessageDto) ->
                        kafkaSendSmsStatusTemplate.send(kafkaConfig.getSmsSendResultTopicName(), messageKey, smsResultMessageDto)
        );
    }

    private static String getMessageKey(SmsResultMessageDto smsResultMessageDto) {
        return String.format("%s", smsResultMessageDto.smsId());
    }
}
