package com.kivilev.service.provider;

import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.model.SmsStatusResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class BeelineSmsProviderStubService implements SmsProviderService {
    private final static Logger logger = LoggerFactory.getLogger(BeelineSmsProviderStubService.class);

    @Override
    public SmsStatusResultInfo sendSms(Sms sms) {
        if (getRandomInt(0, 2) != 0) {
            logger.debug(String.format("sms was successful sent to provider. smsId=%s", sms.getSmsId()));

            return new SmsStatusResultInfo(SmsState.SENT_TO_PROVIDER, SmsResult.SUCCESSFUL_PROCESSED, null, null);
        } else {
            logger.debug(String.format("sending sms to provider was failed. smsId=%s", sms.getSmsId()));

            return new SmsStatusResultInfo(SmsState.SENT_TO_PROVIDER, SmsResult.ERROR, "110", "Some error occurred");
        }
    }

    @Override
    public SmsStatusResultInfo getSmsStatus(String externalId) {
        if (getRandomInt(0, 2) != 0) {
            logger.debug(String.format("sms was successful sent to client. externalId=%s", externalId));

            return new SmsStatusResultInfo(SmsState.SENT_TO_CLIENT, SmsResult.SUCCESSFUL_PROCESSED, null, null);
        } else {
            logger.debug(String.format("sending sms to client was failed. externalId=%s", externalId));

            return new SmsStatusResultInfo(SmsState.SENT_TO_CLIENT, SmsResult.ERROR, "100", "Beeline sms gateway internal error");
        }
    }

    private int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
