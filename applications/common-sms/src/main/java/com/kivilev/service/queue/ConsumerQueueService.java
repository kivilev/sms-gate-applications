package com.kivilev.service.queue;

import com.kivilev.service.queue.model.SmsResultMessageDto;

import java.util.List;

public interface ConsumerQueueService {
    void readSmsStatusMessages(List<SmsResultMessageDto> smsStatusMessageDtos);
}
