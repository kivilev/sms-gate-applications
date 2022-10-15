package com.kivilev.service;

import com.kivilev.dao.SmsDao;
import com.kivilev.model.Sms;
import com.kivilev.model.SmsResult;
import com.kivilev.model.SmsState;
import com.kivilev.model.SmsStateDetail;
import com.kivilev.utils.MillisConstants;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    void processIncomingSmsRequest() {
        var sourceId = "telegram";
        var idempotencyKey = UUID.randomUUID().toString();
        var clientId = 1L;

        if (smsDao.isSmsExists(sourceId, idempotencyKey)) {
            return;
        }

        smsDao.saveSms(
                new Sms(null,
                        sourceId,
                        clientId,
                        idempotencyKey,
                        "sms text",
                        "+7-913-937-5656",
                        new SmsStateDetail(SmsState.NEW_SMS, SmsResult.SUCCESSFUL_PROCESSED, null, null),
                        true)
        );
    }
}
