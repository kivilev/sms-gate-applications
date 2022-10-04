package com.kivilev.provider;

import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsStatusResultInfo;

public interface SmsProviderService {
    SmsStatusResultInfo sendSms(Sms sms);

    SmsStatusResultInfo getSmsStatus(String externalId);
}
