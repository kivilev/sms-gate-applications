package com.kivilev.service.processor;

import com.kivilev.dao.DbSmsDao;
import com.kivilev.dao.SmsDao;
import com.kivilev.exception.ProcessingException;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.model.SmsStateDetail;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DequeSmsMessagesSmsStateProcessorTest {

    private static final String SMS_ID = "2";
    private static final String PROVIDER_EXTERNAL_ID = "external-id";
    private static final String SMS_TEXT = "sms-text";
    private static final String RECEIVER_PHONE_NUMBER = "000000";
    private static final String ERROR_TEXT = "Something goes wrong";

    private final SmsDao smsDao = mock(DbSmsDao.class);
    private final InitialSmsStateProcessor processor = new DequeSmsMessagesSmsStateProcessor(smsDao);

    @Test
    public void processNewSmsMessagesShouldBeSuccesfull() {
        var sms = buildSms();
        var smsMessages = List.of(sms);
        when(smsDao.findBySmsIds(any())).thenReturn(Collections.emptyList());

        processor.process(smsMessages);

        verify(smsDao, times(1)).saveSmsMessages(smsMessages);
    }

    @Test
    public void processExistingSmsMessagesShouldBeSuccesfull() {
        var sms = buildSms();
        var smsMessages = List.of(sms);
        when(smsDao.findBySmsIds(any())).thenReturn(List.of(sms));

        processor.process(smsMessages);

        verify(smsDao, times(1)).saveSmsMessages(Collections.emptyList());
    }

    @Test
    public void processNewSmsMessagesWithErrorShouldThrowCorrectException() {
        var sms = buildSms();
        when(smsDao.findBySmsIds(any())).thenThrow(new RuntimeException(ERROR_TEXT));

        var exception = assertThrows(ProcessingException.class, () -> processor.process(List.of(sms)));

        assertEquals(ERROR_TEXT, exception.getMessage());
    }

    private Sms buildSms() {
        return new Sms(SMS_ID, PROVIDER_EXTERNAL_ID, SMS_TEXT, RECEIVER_PHONE_NUMBER,
                new SmsStateDetail(SmsState.NEW_FROM_QUEUE, SmsResult.NOT_PROCESSED, null, null),
                false,
                true);
    }
}