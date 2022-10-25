package com.kivilev.service.queue;

import com.kivilev.model.Sms;

import java.util.List;

public interface ProducerQueueSmsService {
    void sendNewSmsMessages(List<Sms> smsList);
}
