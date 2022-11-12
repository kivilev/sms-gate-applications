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
    private static final Logger LOGGER = LoggerFactory.getLogger(BeelineSmsProviderStubService.class);

    @Override
    public SmsStateDetail sendSms(Sms sms) {
        if (getRandomInt(0, 4) == 0) {
            LOGGER.debug("sending sms to provider was failed. smsId={}", sms.getSmsId());
            return new SmsStateDetail(SmsState.SENT_TO_PROVIDER, SmsResult.ERROR, "110", "Some error occurred");
        } else {
            LOGGER.debug("sms was successful sent to provider. smsId={}", sms.getSmsId());

            return new SmsStateDetail(SmsState.SENT_TO_PROVIDER, SmsResult.SUCCESSFUL_PROCESSED, null, null);
        }
    }

    @Override
    public SmsStateDetail getSmsStatus(Sms sms) {
        if (getRandomInt(0, 4) == 0) {
            LOGGER.debug("sending sms to client was failed. externalId={}", sms.getSmsId());

            return new SmsStateDetail(SmsState.SENT_TO_CLIENT, SmsResult.ERROR, "100", "Beeline sms gateway internal error");
        } else {
            LOGGER.debug("sms was successful sent to client. externalId={}", sms.getProviderExternalId());

            return new SmsStateDetail(SmsState.SENT_TO_CLIENT, SmsResult.SUCCESSFUL_PROCESSED, null, null);
        }
    }

    private int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
