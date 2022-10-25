package com.kivilev.service;

import com.kivilev.model.Sms;
import com.kivilev.model.SmsResultChangeMessage;

import java.util.List;

public interface SmsService {
    void sendSmsToSmsGateway();

    void processSmsStatusMessages(List<SmsResultChangeMessage> smsResultChangeMessages);

    Sms processIncomingSmsRequest(Sms sms);

    List<Sms> getSmsMessages(Long clientId, int limit);
}
