package com.kivilev.service;

import com.kivilev.dao.SmsDao;
import com.kivilev.model.Sms;
import com.kivilev.model.SmsState;
import com.kivilev.model.SmsStatusInfo;
import com.kivilev.utils.MillisConstants;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class IncomingRequestProcessorImpl {

    private final SmsDao smsDao;
    private final AtomicInteger counter = new AtomicInteger(0);

    public IncomingRequestProcessorImpl(SmsDao smsDao) {
        this.smsDao = smsDao;
    }

    @Scheduled(initialDelay = MillisConstants.SECOND * 10, fixedRate = MillisConstants.MILLIS * 10)
    void processIncomingSmsRequest() {
        var sourceId = "telegram";
        var idempotencyKey = UUID.randomUUID().toString();

        if (smsDao.isSmsExists(sourceId, idempotencyKey)) {
            return;
        }

        smsDao.saveSms(
                new Sms(String.valueOf(counter.incrementAndGet()),
                        sourceId,
                        idempotencyKey,
                        "sms text",
                        "+7-913-937-5656",
                        new SmsStatusInfo(SmsState.NEW_SMS, null, null))
        );
    }
}
