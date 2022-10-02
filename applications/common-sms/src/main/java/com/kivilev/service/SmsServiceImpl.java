package com.kivilev.service;

import com.kivilev.dao.SmsDao;
import com.kivilev.model.Sms;
import com.kivilev.model.SmsState;
import com.kivilev.model.SmsStatusChangeMessage;
import com.kivilev.model.SmsStatusInfo;
import com.kivilev.service.queue.ProducerQueueSmsService;
import com.kivilev.utils.MillisConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@Service
public class SmsServiceImpl implements SmsService {
    private final static Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    private final ProducerQueueSmsService producerQueueSmsService;
    private final AtomicInteger id = new AtomicInteger(0);
    private final SmsDao smsDao;
    private static final Predicate<Sms> filterForNewSms = sms -> sms.getSmsStatusInfo().getSmsStatus() == SmsState.NEW_SMS;
    private final int package_size = 10; // TODO : перейти на конфигурацию

    public SmsServiceImpl(ProducerQueueSmsService producerQueueSmsService, SmsDao smsDao) {
        this.producerQueueSmsService = producerQueueSmsService;
        this.smsDao = smsDao;
    }

    @Override
    @Scheduled(initialDelay = MillisConstants.SECOND * 10, fixedRate = MillisConstants.MILLIS * 10)
    public void sendSmsToSmsGateway() {
        var smsListForSending = smsDao.getSmsList(filterForNewSms, package_size);
        producerQueueSmsService.sendNewSmsMessages(smsListForSending);

        smsListForSending.forEach(sms -> {
            smsDao.setSmsStatus(sms.getSmsId(), new SmsStatusInfo(SmsState.SENT_TO_SMS_GATE, null, null));
            logger.debug(String.format("Sending sms to kafka. smsId: %s", sms.getSmsId()));
        });
    }

    @Override
    public void processSmsStatusMessages(List<SmsStatusChangeMessage> smsStatusChangeMessages) {
        smsStatusChangeMessages.forEach(message -> {
            smsDao.setSmsStatus(
                    message.getSmsId(),
                    new SmsStatusInfo(message.getSmsState(), message.getErrorCode(), message.getErrorMessage())
            );
            logger.debug(String.format("got sms status change message. smsId: %s", message.getSmsId()));
        });
    }
}
