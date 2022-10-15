package com.kivilev.service;

import com.kivilev.service.model.Sms;

import java.util.List;

public interface CommonSmsService {

    Sms sendSms(Sms sms);

    List<Sms> getSmsMessages(Long clientId, Long limit);
}
