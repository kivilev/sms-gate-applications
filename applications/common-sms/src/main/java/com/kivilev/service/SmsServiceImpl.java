package com.kivilev.service;

import com.kivilev.config.SmsStateProcessingConfig;
import com.kivilev.dao.SmsDao;
import com.kivilev.model.Sms;
import com.kivilev.model.SmsResult;
import com.kivilev.model.SmsResultChangeMessage;
import com.kivilev.model.SmsState;
import com.kivilev.model.SmsStateDetail;
import com.kivilev.service.queue.ProducerQueueSmsService;
import com.kivilev.utils.MillisConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@EnableConfigurationProperties(SmsStateProcessingConfig.class)
public class SmsServiceImpl implements SmsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsServiceImpl.class);

    private final ProducerQueueSmsService producerQueueSmsService;
    private final AtomicInteger id = new AtomicInteger(0);
    private final SmsDao smsDao;
    private final SmsStateProcessingConfig smsStateProcessingConfig;

    public SmsServiceImpl(ProducerQueueSmsService producerQueueSmsService, SmsDao smsDao, SmsStateProcessingConfig smsStateProcessingConfig) {
        this.producerQueueSmsService = producerQueueSmsService;
        this.smsDao = smsDao;
        this.smsStateProcessingConfig = smsStateProcessingConfig;
    }

    @Override
    @Transactional
    public Sms processIncomingSmsRequest(Sms sms) {
        var smsOptional = smsDao.getSms(sms.getClientId(), sms.getSourceId(), sms.getSourceIdempotencyKey());
        if (smsOptional.isPresent()) {
            LOGGER.debug(String.format("Processing income sms. Sms already existed. SmsId: %s", smsOptional.get().getSmsId()));
            return smsOptional.get();
        }
        smsDao.saveSms(sms);
        LOGGER.debug(String.format("Processing income sms. New sms. SmsId: %s", sms.getSmsId()));
        return sms;
    }

    @Override
    public List<Sms> getSmsMessages(Long clientId, int limit) {
        return smsDao.getSmsMessages(clientId, limit);
    }

    @Override
    @Scheduled(initialDelay = MillisConstants.SECOND * 10, fixedRate = MillisConstants.MILLIS * 10)
    public void sendSmsToSmsGateway() {
        var smsListForSending = smsDao.getSmsMessages(SmsState.NEW_SMS, SmsResult.SUCCESSFUL_PROCESSED, smsStateProcessingConfig.getPackageSize());
        if (smsListForSending.size() == 0) {
            return;
        }

        producerQueueSmsService.sendNewSmsMessages(smsListForSending);

        // TODO: переписать на обработку ошибок по отдельной смс (сейчас отваливается сразу пачка)
        smsListForSending.forEach(sms -> {
            sms.setSmsStatusInfo(new SmsStateDetail(SmsState.SENT_TO_SMS_GATE, SmsResult.SUCCESSFUL_PROCESSED, null, null));
            smsDao.saveSms(sms);
            LOGGER.debug(String.format("Sending sms to kafka. SmsId: %s", sms.getSmsId()));
        });
    }

    @Override
    public void processSmsStatusMessages(List<SmsResultChangeMessage> smsResultChangeMessages) {
        smsResultChangeMessages.forEach(message -> {
            var smsOptional = smsDao.getSms(message.getSmsId());
            smsOptional.ifPresent(sms -> {
                sms.getSmsStatusInfo().setSmsStatus(SmsState.SENT_TO_CLIENT);
                sms.getSmsStatusInfo().setSmsResult(message.getSmsResult());
                sms.getSmsStatusInfo().setErrorCode(message.getErrorCode());
                sms.getSmsStatusInfo().setErrorMessage(message.getErrorMessage());
                smsDao.saveSms(sms);
            });
            LOGGER.debug(String.format("Getting sms result change message. SmsId: %s", message.getSmsId()));
        });
    }
}
