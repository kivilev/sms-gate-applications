package com.kivilev.service;

import com.kivilev.model.SmsStatusChangeMessage;

import java.util.List;

public interface SmsService {
    void sendSmsToSmsGateway();

    void processSmsStatusMessages(List<SmsStatusChangeMessage> smsStatusChangeMessages);
}
