package com.kivilev.provider;

import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.model.SmsStateDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class BeelineSmsProviderStubService implements SmsProviderService {
    private final static Logger logger = LoggerFactory.getLogger(BeelineSmsProviderStubService.class);

    @Override
    public SmsStateDetail sendSms(Sms sms) {
        if (getRandomInt(0, 4) == 0) {
            logger.debug(String.format("sending sms to provider was failed. smsId=%s", sms.getSmsId()));
            return new SmsStateDetail(SmsState.SENT_TO_PROVIDER, SmsResult.ERROR, "110", "Some error occurred");
        } else {
            logger.debug(String.format("sms was successful sent to provider. smsId=%s", sms.getSmsId()));

            return new SmsStateDetail(SmsState.SENT_TO_PROVIDER, SmsResult.SUCCESSFUL_PROCESSED, null, null);
        }
    }

    @Override
    public SmsStateDetail getSmsStatus(Sms sms) {
        if (getRandomInt(0, 4) == 0) {
            logger.debug(String.format("sending sms to client was failed. externalId=%s", sms.getSmsId()));

            return new SmsStateDetail(SmsState.SENT_TO_CLIENT, SmsResult.ERROR, "100", "Beeline sms gateway internal error");
        } else {
            logger.debug(String.format("sms was successful sent to client. externalId=%s", sms.getProviderExternalId()));

            return new SmsStateDetail(SmsState.SENT_TO_CLIENT, SmsResult.SUCCESSFUL_PROCESSED, null, null);
        }
    }

    private int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
