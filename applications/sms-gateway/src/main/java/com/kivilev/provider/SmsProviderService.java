package com.kivilev.provider;

import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsStateDetail;

public interface SmsProviderService {
    SmsStateDetail sendSms(Sms sms);

    SmsStateDetail getSmsStatus(Sms sms);
}
