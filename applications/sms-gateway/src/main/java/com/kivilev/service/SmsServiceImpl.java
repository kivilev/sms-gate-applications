package com.kivilev.service;

import com.kivilev.config.SmsStateProcessingConfig;
import com.kivilev.dao.SmsDao;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.processor.InitialSmsStateProcessor;
import com.kivilev.service.processor.SmsStateProcessor;
import com.kivilev.service.utils.MillisConstants;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@EnableConfigurationProperties(SmsStateProcessingConfig.class)
public class SmsServiceImpl implements SmsService {

    private final SmsDao smsDao;
    private final Map<SmsState, SmsStateProcessor> smsStateProcessors;
    private final InitialSmsStateProcessor initialSmsStateProcessor;

    public SmsServiceImpl(SmsDao smsDao,
                          InitialSmsStateProcessor initialSmsStateProcessor,
                          Map<SmsState, SmsStateProcessor> smsStateProcessors) {
        this.smsDao = smsDao;
        this.smsStateProcessors = smsStateProcessors;
        this.initialSmsStateProcessor = initialSmsStateProcessor;
    }

    @Override
    public void processNewSmsMessages(List<Sms> smsList) {
        initialSmsStateProcessor.process(smsList);
    }

    @Scheduled(initialDelay = MillisConstants.SECOND * 10, fixedRate = MillisConstants.MILLIS * 10)
    @Override
    public void processSmsReadySendToProvider() {
        smsStateProcessors.get(SmsState.NEW_FROM_QUEUE).process();
    }

    @Scheduled(initialDelay = MillisConstants.SECOND * 10, fixedRate = MillisConstants.MILLIS * 10)
    @Override
    public void getSmsStatusesFromProvider() {
        smsStateProcessors.get(SmsState.SENT_TO_PROVIDER).process();
    }

    @Scheduled(initialDelay = MillisConstants.SECOND * 10, fixedRate = MillisConstants.MILLIS * 10)
    @Override
    public void sendSmsStatusesToQueue() {
        smsStateProcessors.get(SmsState.SENT_TO_CLIENT).process();
    }
}
