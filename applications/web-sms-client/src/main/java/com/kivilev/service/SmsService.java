package com.kivilev.service;

import com.kivilev.service.model.Sms;

import java.util.List;

public interface SmsService {
    Sms sendNewSms(Sms sms);

    List<Sms> getSmsMessages(Long clientId, int limit);
}
