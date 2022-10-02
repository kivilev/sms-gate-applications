package com.kivilev.service.queue;

import com.kivilev.service.queue.model.SmsSendMessageDto;

import java.util.List;

public interface ConsumerQueueService {
    void readSmsSendMessages(List<SmsSendMessageDto> smsSendMessageDtos);
}
