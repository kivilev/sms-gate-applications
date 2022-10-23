package com.kivilev.service.grpc.mapper;

import com.google.protobuf.Timestamp;
import com.kivilev.protobuf.generated.SmsMessageResponse;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsStatus;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GrpcObjectMapperTest {
    private final static Clock clock = Clock.systemUTC();
    private static final Long CLIENT_ID = 1L;
    private static final Long SMS_ID = 2L;
    private static final String SOURCE_ID = "sourceId";
    private static final String IDEMPOTENCY_KEY = "idempotency-key";
    private static final String SMS_TEXT = "sms-text";
    private static final String RECEIVER_PHONE_NUMBER = "000000";
    private static final SmsStatus SMS_STATUS = SmsStatus.PROCESSING;
    private static final ZonedDateTime CREATE_DATE_TIME = ZonedDateTime.of(2000, 01, 01, 01, 01, 01, 00, clock.getZone());
    private static final ZonedDateTime UPDATE_DATE_TIME = ZonedDateTime.of(2020, 01, 01, 01, 01, 01, 00, clock.getZone());
    private static final Timestamp CREATE_TIMESTAMP = Timestamp.newBuilder()
            .setSeconds(CREATE_DATE_TIME.toEpochSecond())
            .setNanos(CREATE_DATE_TIME.getNano())
            .build();
    private static final Timestamp UPDATE_TIMESTAMP = Timestamp.newBuilder()
            .setSeconds(UPDATE_DATE_TIME.toEpochSecond())
            .setNanos(UPDATE_DATE_TIME.getNano())
            .build();

    private final GrpcObjectMapper objectMapper = new GrpcObjectMapper(clock);

    @Test
    void MappingSmsToSendSmsRequestShouldBeSuccesfull() {
        Sms sms = new Sms(CLIENT_ID, SOURCE_ID, IDEMPOTENCY_KEY, SMS_TEXT, RECEIVER_PHONE_NUMBER, SMS_STATUS);
        sms.setCreateDateTime(CREATE_DATE_TIME);

        var actualRequest = objectMapper.toSendSmsRequest(sms);

        assertNotNull(actualRequest);
        assertEquals(CLIENT_ID, actualRequest.getClientId());
        assertEquals(IDEMPOTENCY_KEY, actualRequest.getIdempotencyKey());
        assertEquals(SOURCE_ID, actualRequest.getSource());
        assertEquals(SMS_TEXT, actualRequest.getSmsText());
        assertEquals(RECEIVER_PHONE_NUMBER, actualRequest.getReceiverPhoneNumber());
        assertEquals(CREATE_TIMESTAMP, actualRequest.getCreateDateTime());
    }

    @Test
    void MappingGetSmsStatusMessageResponseToSmsShouldBeSuccesfull() {
        var response = SmsMessageResponse.newBuilder()
                .setSmsId(SMS_ID)
                .setClientId(CLIENT_ID)
                .setSource(SOURCE_ID)
                .setIdempotencyKey(IDEMPOTENCY_KEY)
                .setSmsText(SMS_TEXT)
                .setReceiverPhoneNumber(RECEIVER_PHONE_NUMBER)
                .setCreateDateTime(CREATE_TIMESTAMP)
                .setSendReceiverDateTime(UPDATE_TIMESTAMP)
                .setStatus(com.kivilev.protobuf.generated.SmsStatus.SENT)
                .build();

        var actualSms = objectMapper.toSms(response);

        assertNotNull(actualSms);
        assertEquals(SMS_ID, actualSms.getSmsId());
        assertEquals(CLIENT_ID, actualSms.getClientId());
        assertEquals(SOURCE_ID, actualSms.getSourceId());
        assertEquals(IDEMPOTENCY_KEY, actualSms.getIdempotencyKey());
        assertEquals(SMS_TEXT, actualSms.getSmsText());
        assertEquals(RECEIVER_PHONE_NUMBER, actualSms.getReceiverPhoneNumber());
        assertEquals(CREATE_DATE_TIME, actualSms.getCreateDateTime());
        assertEquals(UPDATE_DATE_TIME, actualSms.getSendReceiverDateTime());
    }
}