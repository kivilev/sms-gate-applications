package com.kivilev.service.processor;

import com.kivilev.config.SmsStateProcessingConfig;
import com.kivilev.dao.SmsDao;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.provider.SmsProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
@EnableConfigurationProperties(SmsStateProcessingConfig.class)
public class SendingToProviderSmsStateProcessor implements SmsStateProcessor {

    private final SmsState processingSmsState = SmsState.NEW_FROM_QUEUE;
    private final SmsDao smsDao;
    private final SmsStateProcessingConfig smsStateProcessingConfig;
    private final SmsProviderService smsProviderService;
    private final Predicate<Sms> processingFilter = (Sms sms) -> sms.getSmsStatusInfo().getSmsStatus() == processingSmsState &&
            sms.getSmsStatusInfo().getSmsResult() == SmsResult.SUCCESSFUL_PROCESSED;
    private final Logger logger = LoggerFactory.getLogger(SendingToProviderSmsStateProcessor.class);

    public SendingToProviderSmsStateProcessor(SmsDao smsDao, SmsStateProcessingConfig smsStateProcessingConfig, SmsProviderService smsProviderService) {
        this.smsDao = smsDao;
        this.smsStateProcessingConfig = smsStateProcessingConfig;
        this.smsProviderService = smsProviderService;
    }

    @Override
    public void process() {
        var smsList = smsDao.getSmsList(processingFilter, smsStateProcessingConfig.getPackageSize());
        smsList.forEach(sms -> {
            var smsStatusInfo = smsProviderService.sendSms(sms);
            smsDao.saveSmsStatusResultInfo(sms.getSmsId(), smsStatusInfo);
        });
    }

    @Override
    public SmsState getSmsStatus() {
        return processingSmsState;
    }
}
