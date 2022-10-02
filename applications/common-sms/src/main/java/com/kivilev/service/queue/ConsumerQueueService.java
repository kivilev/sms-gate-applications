package com.kivilev.service.queue;

import com.kivilev.service.queue.model.SmsStatusChangeMessageDto;

import java.util.List;

public interface ConsumerQueueService {
    void readSmsStatusMessages(List<SmsStatusChangeMessageDto> smsStatusMessageDtos);
}
