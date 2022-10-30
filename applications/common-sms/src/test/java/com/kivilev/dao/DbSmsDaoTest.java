package com.kivilev.dao;

import com.kivilev.model.Sms;
import com.kivilev.model.SmsResult;
import com.kivilev.model.SmsState;
import com.kivilev.model.SmsStateDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class DbSmsDaoTest {
    // TODO: Покрыты не все тестовые кейсы
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2022-01-01T01:00:00Z"), ZoneOffset.UTC);
    private static final Long CLIENT_ID = 1L;
    private static final String SOURCE_ID = "sourceId";
    private static final String SMS_TEXT = "sms-text";
    private static final String RECEIVER_PHONE_NUMBER = "000000";
    private static final ZonedDateTime CREATE_DATE_TIME = ZonedDateTime.now(CLOCK).minusMinutes(1);
    private static final ZonedDateTime UPDATE_DATE_TIME = ZonedDateTime.now(CLOCK);

    @Autowired
    SmsDao smsDao;

    @Test
    public void getNotExistedSmsShouldNotReturnSms() {
        var idempotencyKey = randomString();
        var smsOptional = smsDao.getSms(CLIENT_ID, SOURCE_ID, idempotencyKey);
        assertFalse(smsOptional.isPresent());
    }

    @Test
    public void getExistedSmsShouldReturnSms() {
        var idempotencyKey = randomString();
        Sms sms = buildSms(idempotencyKey);
        smsDao.saveSms(sms);

        var smsOptional = smsDao.getSms(CLIENT_ID, SOURCE_ID, idempotencyKey);

        assertTrue(smsOptional.isPresent());
        var actualSms = smsOptional.get();
        assertEquals(sms.getSmsId(), actualSms.getSmsId());
        assertEquals(SOURCE_ID, actualSms.getSourceId());
        assertEquals(CLIENT_ID, actualSms.getClientId());
        assertEquals(SMS_TEXT, actualSms.getSmsText());
        assertEquals(idempotencyKey, actualSms.getSourceIdempotencyKey());
        assertEquals(RECEIVER_PHONE_NUMBER, actualSms.getReceiverPhoneNumber());
        assertEquals(CREATE_DATE_TIME, map(actualSms.getCreateDateTime()));
        assertEquals(UPDATE_DATE_TIME, map(actualSms.getChangeStatusDateTime()));

        var actualSmsStatusInfo = actualSms.getSmsStatusInfo();
        assertNotNull(actualSmsStatusInfo);
        assertEquals(SmsState.NEW_SMS, actualSmsStatusInfo.getSmsState());
        assertEquals(SmsResult.SUCCESSFUL_PROCESSED, actualSmsStatusInfo.getSmsResult());
        assertNull(actualSmsStatusInfo.getErrorCode());
        assertNull(actualSmsStatusInfo.getErrorMessage());
    }

    @Test
    public void getSmsMessagesByClientIdWithLimitShouldBeCorrectly() {
        final var generationSize = 10;
        final var limit = 5;
        for (int i = 0; i < generationSize; i++) {
            smsDao.saveSms(buildSms(randomString()));
        }

        var smsMessages = smsDao.getSmsMessages(CLIENT_ID, limit);

        // TODO: Это некорректный тест, нужно сравнивать с LIMIT.
        //  Сделано так, чтобы после починки метода getSmsMessages он сломался
        assertEquals(generationSize, smsMessages.size());
        smsMessages.forEach(sms -> assertEquals(CLIENT_ID, sms.getClientId()));
    }

    private Sms buildSms(String idempotencyKey) {
        return new Sms(null, SOURCE_ID, CLIENT_ID, idempotencyKey, SMS_TEXT, RECEIVER_PHONE_NUMBER,
                new SmsStateDetail(SmsState.NEW_SMS, SmsResult.SUCCESSFUL_PROCESSED, null, null),
                map(CREATE_DATE_TIME), map(UPDATE_DATE_TIME), true);
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }

    private String map(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(DATE_TIME_FORMATTER);
    }

    private ZonedDateTime map(String dtime) {
        return ZonedDateTime.parse(dtime, DATE_TIME_FORMATTER);
    }
}