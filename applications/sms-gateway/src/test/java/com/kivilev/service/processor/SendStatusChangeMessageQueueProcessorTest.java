package com.kivilev.service.processor;

import com.kivilev.config.SmsStateProcessingConfig;
import com.kivilev.dao.DbSmsDao;
import com.kivilev.dao.SmsDao;
import com.kivilev.exception.ProcessingException;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.model.SmsStateDetail;
import com.kivilev.service.queue.KafkaProducerQueueServiceImpl;
import com.kivilev.service.queue.ProducerQueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SendStatusChangeMessageQueueProcessorTest {

    private static final String SMS_ID = "2";
    private static final String PROVIDER_EXTERNAL_ID = "external-id";
    private static final String SMS_TEXT = "sms-text";
    private static final String RECEIVER_PHONE_NUMBER = "000000";
    private static final String ERROR_TEXT = "Something goes wrong";
    private static final int PACKAGE_SIZE = 10;

    private final SmsStateProcessingConfig smsStateProcessingConfig = mock(SmsStateProcessingConfig.class);
    private final SmsDao smsDao = mock(DbSmsDao.class);
    private final ProducerQueueService producerQueueService = mock(KafkaProducerQueueServiceImpl.class);
    private final SmsStateProcessor processor = new SendStatusChangeMessageQueueProcessor(smsDao, smsStateProcessingConfig, producerQueueService);

    @BeforeEach
    void beforeEachTest() {
        when(smsStateProcessingConfig.getPackageSize()).thenReturn(PACKAGE_SIZE);
    }

    @Test
    public void processSmsMessagesShouldBeSuccesfull() {
        var sms = buildSms();
        var smsMessages = List.of(sms);
        when(smsDao.getSmsMessagesReadyForSendingQueue(eq(PACKAGE_SIZE))).thenReturn(smsMessages);

        processor.process();

        verify(smsDao, times(1)).saveSms(sms);
    }

    @Test
    public void processEmptySmsMessagesShouldBeSuccesfull() {
        when(smsDao.getSmsMessagesReadyForSendingQueue(eq(PACKAGE_SIZE))).thenReturn(Collections.emptyList());

        processor.process();

        verify(smsDao, never()).saveSms(any());
    }

    @Test
    public void processSmsMessagesWithErrorShouldThrowCorrectException() {
        when(smsDao.getSmsMessagesReadyForSendingQueue(eq(PACKAGE_SIZE))).thenThrow(new RuntimeException(ERROR_TEXT));

        var exception = assertThrows(ProcessingException.class, processor::process);

        assertEquals(ERROR_TEXT, exception.getMessage());
    }

    private Sms buildSms() {
        return new Sms(SMS_ID, PROVIDER_EXTERNAL_ID, SMS_TEXT, RECEIVER_PHONE_NUMBER,
                new SmsStateDetail(SmsState.NEW_FROM_QUEUE, SmsResult.SUCCESSFUL_PROCESSED, null, null),
                false,
                true);
    }
}