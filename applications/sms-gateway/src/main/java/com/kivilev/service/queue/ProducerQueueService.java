package com.kivilev.service.queue;

import com.kivilev.service.model.Sms;

import java.util.List;

public interface ProducerQueueService {
    void sendSmsStatusMessages(List<Sms> smsList);
}
