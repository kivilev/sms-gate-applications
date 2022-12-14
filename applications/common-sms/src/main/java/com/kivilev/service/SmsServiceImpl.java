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

@Service
@EnableConfigurationProperties(SmsStateProcessingConfig.class)
public class SmsServiceImpl implements SmsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsServiceImpl.class);

    private final ProducerQueueSmsService producerQueueSmsService;
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
            LOGGER.debug("Processing income sms. Sms already existed. SmsId: {}", smsOptional.get().getSmsId());
            return smsOptional.get();
        }
        smsDao.saveSms(sms);
        LOGGER.debug("Processing income sms. New sms. SmsId: {}", sms.getSmsId());
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
        if (smsListForSending.isEmpty()) {
            return;
        }

        LOGGER.debug("Sending sms messages to kafka. Count: {}", smsListForSending.size());

        producerQueueSmsService.sendNewSmsMessages(smsListForSending);

        // TODO: ???????????????????? ???? ?????????????????? ???????????? ???? ?????????????????? ?????? (???????????? ???????????????????????? ?????????? ??????????)
        smsListForSending.forEach(sms -> {
            sms.setSmsStateDetail(new SmsStateDetail(SmsState.SENT_TO_SMS_GATE, SmsResult.SUCCESSFUL_PROCESSED, null, null));
            smsDao.saveSms(sms);
            LOGGER.debug("Sending sms to kafka. SmsId: {}", sms.getSmsId());
        });
    }

    @Override
    public void processSmsStatusMessages(List<SmsResultChangeMessage> smsResultChangeMessages) {
        smsResultChangeMessages.forEach(message -> {
            var smsOptional = smsDao.getSms(Long.valueOf(message.getSmsId()));
            smsOptional.ifPresent(sms -> {
                sms.getSmsStateDetail().setSmsStatus(SmsState.SENT_TO_CLIENT);
                sms.getSmsStateDetail().setSmsResult(message.getSmsResult());
                sms.getSmsStateDetail().setErrorCode(message.getErrorCode());
                sms.getSmsStateDetail().setErrorMessage(message.getErrorMessage());
                smsDao.saveSms(sms);
            });
            LOGGER.debug("Getting sms result change message. SmsId: {}", message.getSmsId());
        });
    }
}
