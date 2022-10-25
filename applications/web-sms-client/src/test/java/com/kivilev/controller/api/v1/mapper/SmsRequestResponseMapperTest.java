package com.kivilev.controller.api.v1.mapper;

import com.kivilev.controller.api.v1.model.SendNewSmsRequestDto;
import com.kivilev.controller.api.v1.model.SmsStatusDto;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsStatus;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SmsRequestResponseMapperTest {

    private final SmsRequestResponseMapper smsRequestResponseMapper = new SmsRequestResponseMapper();

    private final static Clock clock = Clock.systemUTC();
    private final static Long CLIENT_ID = 1L;
    private final static String SMS_TEXT = "sms-text";
    private final static String RECEIVER_PHONE_NUMBER = "0000000";
    private final static String IDEMPOTENCY_KEY = "key";
    private static final Long SMS_ID = 2L;
    private static final String SOURCE_ID = "sourceId";
    private static final SmsStatus SMS_STATUS = SmsStatus.PROCESSING;
    private static final ZonedDateTime CREATE_DATE_TIME = ZonedDateTime.of(2000, 01, 01, 01, 01, 01, 00, clock.getZone());
    private static final ZonedDateTime UPDATE_DATE_TIME = ZonedDateTime.of(2020, 01, 01, 01, 01, 01, 00, clock.getZone());

    @Test
    public void MappingNewSmsRequestDtoToSmsShouldBeSuccesfull() {
        var requestDto = new SendNewSmsRequestDto(CLIENT_ID, SMS_TEXT, RECEIVER_PHONE_NUMBER, IDEMPOTENCY_KEY);

        var actualSms = smsRequestResponseMapper.toSms(requestDto);

        assertNotNull(actualSms);
        assertNull(actualSms.getSmsId());
        assertNull(actualSms.getCreateDateTime());
        assertNull(actualSms.getSendReceiverDateTime());
        assertEquals(CLIENT_ID, actualSms.getClientId());
        assertEquals(SMS_TEXT, actualSms.getSmsText());
        assertEquals(RECEIVER_PHONE_NUMBER, actualSms.getReceiverPhoneNumber());
        assertEquals(IDEMPOTENCY_KEY, actualSms.getIdempotencyKey());
        assertEquals(SmsStatus.PROCESSING, actualSms.getSmsStatus());
    }

    @Test
    public void MappingSmsToSendNewSmsResponseDtoBeSuccesfull() {
        var sms = new Sms(SMS_ID, CLIENT_ID, SOURCE_ID, IDEMPOTENCY_KEY, SMS_TEXT, RECEIVER_PHONE_NUMBER, SmsStatus.SENT, CREATE_DATE_TIME, UPDATE_DATE_TIME);

        var responseDto = smsRequestResponseMapper.toSendNewSmsResponseDto(sms);

        assertNotNull(responseDto);
        assertEquals(SMS_ID, responseDto.smsId());
        assertEquals(SmsStatusDto.SENT, responseDto.smsStatus());
        assertEquals(CREATE_DATE_TIME, responseDto.createDateTime());
        assertEquals(UPDATE_DATE_TIME, responseDto.sendDateTime());
    }
}