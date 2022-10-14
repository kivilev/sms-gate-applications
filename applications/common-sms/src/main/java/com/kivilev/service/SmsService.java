package com.kivilev.service;

import com.kivilev.model.SmsResultChangeMessage;

import java.util.List;

public interface SmsService {
    void sendSmsToSmsGateway();

    void processSmsStatusMessages(List<SmsResultChangeMessage> smsResultChangeMessages);
}
