package com.kivilev.service.processor;

import com.kivilev.config.SmsStateProcessingConfig;
import com.kivilev.dao.DbSmsDao;
import com.kivilev.dao.SmsDao;
import com.kivilev.exception.ProcessingException;
import com.kivilev.provider.BeelineSmsProviderStubService;
import com.kivilev.provider.SmsProviderService;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.model.SmsStateDetail;
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

class GetStatusFromProviderSmsStateProcessorTest {
    private static final String SMS_ID = "2";
    private static final String PROVIDER_EXTERNAL_ID = "external-id";
    private static final String SMS_TEXT = "sms-text";
    private static final String RECEIVER_PHONE_NUMBER = "000000";
    private static final String ERROR_TEXT = "Something goes wrong";
    private static final int PACKAGE_SIZE = 10;

    private final SmsStateProcessingConfig smsStateProcessingConfig = mock(SmsStateProcessingConfig.class);
    private final SmsProviderService smsProviderService = mock(BeelineSmsProviderStubService.class);
    private final SmsDao smsDao = mock(DbSmsDao.class);
    private final SmsStateProcessor processor = new GetStatusFromProviderSmsStateProcessor(smsDao, smsStateProcessingConfig, smsProviderService);

    @BeforeEach
    void beforeAllTests() {
        when(smsStateProcessingConfig.getPackageSize()).thenReturn(PACKAGE_SIZE);
    }

    @Test
    public void processSmsMessagesShouldBeSuccesfull() {
        var sms = buildSms();
        var smsMessages = List.of(sms);
        var smsStateFromProvider = new SmsStateDetail(SmsState.SENT_TO_CLIENT, SmsResult.SUCCESSFUL_PROCESSED, null, null);
        when(smsDao.getSmsMessages(any(), eq(SmsResult.SUCCESSFUL_PROCESSED), eq(PACKAGE_SIZE))).thenReturn(smsMessages);
        when(smsProviderService.getSmsStatus(sms)).thenReturn(smsStateFromProvider);

        processor.process();

        verify(smsProviderService, times(1)).getSmsStatus(sms);
        verify(smsDao, times(1)).saveSmsStatusResultInfo(sms.getSmsId(), smsStateFromProvider);
    }

    @Test
    public void processEmptySmsMessagesShouldBeSuccesfull() {
        when(smsDao.getSmsMessages(any(), eq(SmsResult.SUCCESSFUL_PROCESSED), eq(PACKAGE_SIZE))).thenReturn(Collections.emptyList());

        processor.process();

        verify(smsProviderService, never()).getSmsStatus(any());
        verify(smsDao, never()).saveSmsStatusResultInfo(any(), any());
    }

    @Test
    public void processSmsMessagesWithErrorShouldThrowCorrectException() {
        when(smsDao.getSmsMessages(any(), eq(SmsResult.SUCCESSFUL_PROCESSED), eq(PACKAGE_SIZE))).thenThrow(new RuntimeException(ERROR_TEXT));

        var exception = assertThrows(ProcessingException.class, processor::process);

        assertEquals(ERROR_TEXT, exception.getMessage());
    }

    private Sms buildSms() {
        return new Sms(SMS_ID, PROVIDER_EXTERNAL_ID, SMS_TEXT, RECEIVER_PHONE_NUMBER,
                new SmsStateDetail(SmsState.SENT_TO_PROVIDER, SmsResult.SUCCESSFUL_PROCESSED, null, null),
                false,
                true);
    }
}