package com.kivilev.service;

import com.kivilev.service.model.Sms;

import java.util.List;

public interface SmsService {

    void processNewSmsMessages(List<Sms> smsList);

    void processSmsReadySendToProvider();

    void getSmsStatusesFromProvider();

    void sendSmsStatusesToQueue();
}
