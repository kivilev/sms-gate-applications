package com.kivilev.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SmsServiceImplTest {

/*    private final SmsDao smsDao = mock(SmsDao.class);
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
                new SmsStatusDetail(SmsState.NEW_FROM_QUEUE, SmsResult.NOT_PROCESSED, null, null),
                false,
                true);
    }*/
}