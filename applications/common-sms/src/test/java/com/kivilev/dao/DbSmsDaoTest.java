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
    private static final String ERROR_CODE = "error-code";
    private static final String ERROR_MESSAGE = "error-message";

    @Autowired
    SmsDao smsDao;

    @Test
    public void gettingNotExistedSmsShouldNotReturnSms() {
        var idempotencyKey = randomString();
        var smsOptional = smsDao.getSms(CLIENT_ID, SOURCE_ID, idempotencyKey);
        assertFalse(smsOptional.isPresent());
    }

    @Test
    public void gettingExistedSmsShouldReturnSms() {
        var idempotencyKey = randomString();
        Sms sms = buildSms(idempotencyKey);
        smsDao.saveSms(sms);
        var smsId = sms.getSmsId();

        var actualSmsOptional = smsDao.getSms(CLIENT_ID, SOURCE_ID, idempotencyKey);

        assertTrue(actualSmsOptional.isPresent());
        var actualSms = actualSmsOptional.get();
        assertSms(idempotencyKey, smsId, actualSms);
    }

    @Test
    public void gettingSmsMessagesByClientIdWithLimitShouldBeCorrectly() {
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

    @Test
    public void gettingExistedSmsByIdShouldReturnSms() {
        var idempotencyKey = randomString();
        var sms = buildSms(idempotencyKey);
        smsDao.saveSms(sms);
        var smsId = sms.getSmsId();

        var actualSmsOptional = smsDao.getSms(smsId);

        assertTrue(actualSmsOptional.isPresent());
        var actualSms = actualSmsOptional.get();
        assertSms(idempotencyKey, smsId, actualSms);
    }

    @Test
    public void gettingNotExistedSmsByIdShouldReturnEmptyResult() {
        var notExistedSmsId = -1L;
        var actualSmsOptional = smsDao.getSms(notExistedSmsId);

        assertFalse(actualSmsOptional.isPresent());
    }

    @Test
    public void gettingSmsMessagesByStateAndResultShouldReturnCorrectMessages() {
        final var generationSize = 10;
        final var limit = 5;
        for (int i = 0; i < generationSize; i++) {
            var sms = buildSms(randomString());
            sms.setSmsStateDetail(new SmsStateDetail(SmsState.NEW_SMS, SmsResult.ERROR, ERROR_CODE, ERROR_MESSAGE));
            smsDao.saveSms(sms);
        }
        for (int i = 0; i < generationSize; i++) {
            var sms = buildSms(randomString());
            sms.setSmsStateDetail(new SmsStateDetail(SmsState.SENT_TO_CLIENT, SmsResult.SUCCESSFUL_PROCESSED, "", ""));
            smsDao.saveSms(sms);
        }

        var smsMessages = smsDao.getSmsMessages(SmsState.NEW_SMS, SmsResult.ERROR, limit);
        assertEquals(limit, smsMessages.size());
        for (var actualSms : smsMessages) {
            assertEquals(SmsState.NEW_SMS, actualSms.getSmsStateDetail().getSmsState());
            assertEquals(SmsResult.ERROR, actualSms.getSmsStateDetail().getSmsResult());
            assertEquals(ERROR_CODE, actualSms.getSmsStateDetail().getErrorCode());
            assertEquals(ERROR_MESSAGE, actualSms.getSmsStateDetail().getErrorMessage());
        }
    }

    private void assertSms(String idempotencyKey, Long smsId, Sms actualSms) {
        assertEquals(smsId, actualSms.getSmsId());
        assertEquals(SOURCE_ID, actualSms.getSourceId());
        assertEquals(CLIENT_ID, actualSms.getClientId());
        assertEquals(SMS_TEXT, actualSms.getSmsText());
        assertEquals(idempotencyKey, actualSms.getSourceIdempotencyKey());
        assertEquals(RECEIVER_PHONE_NUMBER, actualSms.getReceiverPhoneNumber());
        assertEquals(CREATE_DATE_TIME, map(actualSms.getCreateDateTime()));
        assertEquals(UPDATE_DATE_TIME, map(actualSms.getChangeStatusDateTime()));

        var actualSmsStatusInfo = actualSms.getSmsStateDetail();
        assertNotNull(actualSmsStatusInfo);
        assertEquals(SmsState.NEW_SMS, actualSmsStatusInfo.getSmsState());
        assertEquals(SmsResult.SUCCESSFUL_PROCESSED, actualSmsStatusInfo.getSmsResult());
        assertNull(actualSmsStatusInfo.getErrorCode());
        assertNull(actualSmsStatusInfo.getErrorMessage());
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