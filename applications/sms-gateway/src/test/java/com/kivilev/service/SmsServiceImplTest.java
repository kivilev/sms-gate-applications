package com.kivilev.service;

import com.kivilev.dao.SmsDao;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.model.SmsStatusResultInfo;
import com.kivilev.service.processor.InitialSmsStateProcessor;
import com.kivilev.service.processor.SmsStateProcessor;
import com.kivilev.service.queue.ProducerQueueService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SmsServiceImplTest {

    private final SmsDao smsDao = mock(SmsDao.class);
    private final ProducerQueueService producerQueueService = mock(ProducerQueueService.class);
    private final Map<SmsState, SmsStateProcessor> smsStateProcessors = new HashMap<>();
    private final InitialSmsStateProcessor initialSmsStateProcessor = mock(InitialSmsStateProcessor.class);
    private final SmsServiceImpl smsService = new SmsServiceImpl(smsDao, producerQueueService, smsStateProcessors, initialSmsStateProcessor);

    @Test
    void NewSmsMessageWhichNotExistShouldSuccesfullProcessed() {
        var sms = buildSms();
        List<Sms> smsMessages = new ArrayList<>();
        smsMessages.add(sms);

        when(smsDao.findBySmsIds(anyList())).thenReturn(Collections.emptyList());

        smsService.processNewSmsMessages(smsMessages);

        verify(smsDao, times(1)).saveSmsMessages(smsMessages);
    }

    @Test
    void NewSmsMessageWhichExistShouldSuccesfullProcessed() {
        var sms = buildSms();
        List<Sms> smsMessages = new ArrayList<>();
        smsMessages.add(sms);

        when(smsDao.findBySmsIds(anyList())).thenReturn(smsMessages);

        smsService.processNewSmsMessages(smsMessages);

        verify(smsDao, never()).saveSmsMessages(smsMessages);
    }

    private Sms buildSms() {
        return new Sms("sms-id",
                "external_id",
                "sourceId",
                "sms-text",
                "receiverPhone",
                new SmsStatusResultInfo(SmsState.NEW_FROM_QUEUE, SmsResult.NOT_PROCESSED, null, null),
                false,
                true);
    }
}