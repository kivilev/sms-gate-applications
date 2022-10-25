package com.kivilev.service.processor;

import com.kivilev.config.SmsStateProcessingConfig;
import com.kivilev.dao.SmsDao;
import com.kivilev.provider.SmsProviderService;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Predicate;

@Service
@EnableConfigurationProperties(SmsStateProcessingConfig.class)
public class GetStatusFromProviderSmsStateProcessor implements SmsStateProcessor {

    private final SmsState processingSmsState = SmsState.SENT_TO_PROVIDER;
    private final SmsDao smsDao;
    private final SmsStateProcessingConfig smsStateProcessingConfig;
    private final SmsProviderService smsProviderService;
    private final Predicate<Sms> predicate = (Sms sms) -> sms.getSmsStatusDetail().getSmsStatus() == processingSmsState &&
            sms.getSmsStatusDetail().getSmsResult() == SmsResult.SUCCESSFUL_PROCESSED;
    private final Logger logger = LoggerFactory.getLogger(GetStatusFromProviderSmsStateProcessor.class);

    public GetStatusFromProviderSmsStateProcessor(SmsDao smsDao, SmsStateProcessingConfig smsStateProcessingConfig, SmsProviderService smsProviderService) {
        this.smsDao = smsDao;
        this.smsStateProcessingConfig = smsStateProcessingConfig;
        this.smsProviderService = smsProviderService;
    }

    @Transactional
    @Override
    public void process() {
        try {
            var smsList = smsDao.getSmsMessages(
                    processingSmsState,
                    SmsResult.SUCCESSFUL_PROCESSED,
                    smsStateProcessingConfig.getPackageSize()
            );
            smsList.forEach(sms -> {
                var smsStatusInfo = smsProviderService.getSmsStatus(sms);
                smsDao.saveSmsStatusResultInfo(sms.getSmsId(), smsStatusInfo);
            });
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    @Override
    public SmsState getSmsState() {
        return processingSmsState;
    }
}
