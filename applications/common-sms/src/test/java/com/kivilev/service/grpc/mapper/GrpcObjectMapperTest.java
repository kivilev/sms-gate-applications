package com.kivilev.service.grpc.mapper;

import com.google.protobuf.Timestamp;
import com.kivilev.model.Sms;
import com.kivilev.model.SmsResult;
import com.kivilev.model.SmsState;
import com.kivilev.model.SmsStateDetail;
import com.kivilev.protobuf.generated.SendSmsRequest;
import com.kivilev.protobuf.generated.SmsStatus;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class GrpcObjectMapperTest {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2022-01-01T01:00:00Z"), ZoneOffset.UTC);
    private static final Long CLIENT_ID = 1L;
    private static final Long SMS_ID = 2L;
    private static final String SOURCE_ID = "sourceId";
    private static final String IDEMPOTENCY_KEY = "idempotency-key";
    private static final String SMS_TEXT = "sms-text";
    private static final String RECEIVER_PHONE_NUMBER = "000000";
    private static final ZonedDateTime CREATE_DATE_TIME = ZonedDateTime.now(CLOCK).minusMinutes(1);
    private static final ZonedDateTime UPDATE_DATE_TIME = ZonedDateTime.now(CLOCK);
    private static final Timestamp CREATE_TIMESTAMP = Timestamp.newBuilder()
            .setSeconds(CREATE_DATE_TIME.toEpochSecond())
            .setNanos(CREATE_DATE_TIME.getNano())
            .build();
    private static final Timestamp UPDATE_TIMESTAMP = Timestamp.newBuilder()
            .setSeconds(UPDATE_DATE_TIME.toEpochSecond())
            .setNanos(UPDATE_DATE_TIME.getNano())
            .build();

    private final GrpcObjectMapper mapper = new GrpcObjectMapper(CLOCK);

    @Test
    public void mappingSendSmsRequestToSmsShouldBeSuccessful() {
        SendSmsRequest sendSmsRequest = SendSmsRequest.newBuilder()
                .setClientId(CLIENT_ID)
                .setSource(SOURCE_ID)
                .setIdempotencyKey(IDEMPOTENCY_KEY)
                .setReceiverPhoneNumber(RECEIVER_PHONE_NUMBER)
                .setSmsText(SMS_TEXT)
                .setCreateDateTime(CREATE_TIMESTAMP)
                .build();

        var actualSms = mapper.toSms(sendSmsRequest);

        assertNotNull(actualSms);
        assertEquals(CLIENT_ID, actualSms.getClientId());
        assertEquals(SOURCE_ID, actualSms.getSourceId());
        assertEquals(IDEMPOTENCY_KEY, actualSms.getSourceIdempotencyKey());
        assertEquals(RECEIVER_PHONE_NUMBER, actualSms.getReceiverPhoneNumber());
        assertEquals(SMS_TEXT, actualSms.getSmsText());
        assertEquals(toString(CREATE_DATE_TIME), actualSms.getCreateDateTime());
        assertEquals(toString(UPDATE_DATE_TIME), actualSms.getChangeStatusDateTime());
        assertEquals(SmsState.NEW_SMS, actualSms.getSmsStateDetail().getSmsState());
        assertEquals(SmsResult.SUCCESSFUL_PROCESSED, actualSms.getSmsStateDetail().getSmsResult());
        assertNull(actualSms.getSmsStateDetail().getErrorCode());
        assertNull(actualSms.getSmsStateDetail().getErrorMessage());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("smsToSmsMessageResponseTestCases")
    public void mappingSmsToSmsMessageResponseShouldBeSuccessful(String testCaseName, Sms sms, SmsStatus expectedSmsStatus) {
        var actualResponse = mapper.toSmsMessageResponse(sms);

        assertNotNull(actualResponse);
        assertEquals(CLIENT_ID, actualResponse.getClientId());
        assertEquals(SOURCE_ID, actualResponse.getSource());
        assertEquals(IDEMPOTENCY_KEY, actualResponse.getIdempotencyKey());
        assertEquals(RECEIVER_PHONE_NUMBER, actualResponse.getReceiverPhoneNumber());
        assertEquals(SMS_TEXT, actualResponse.getSmsText());
        assertEquals(CREATE_TIMESTAMP, actualResponse.getCreateDateTime());
        assertEquals(UPDATE_TIMESTAMP, actualResponse.getSendReceiverDateTime());
        assertEquals(expectedSmsStatus, actualResponse.getStatus());
    }

    @SuppressFBWarnings("UPM")
    private static Stream<Arguments> smsToSmsMessageResponseTestCases() {
        return Stream.of(
                Arguments.of("sms in new sms state", buildSms(SmsState.NEW_SMS, SmsResult.SUCCESSFUL_PROCESSED), SmsStatus.PROCESSING),
                Arguments.of("sms in sent to sms gate state", buildSms(SmsState.SENT_TO_SMS_GATE, SmsResult.SUCCESSFUL_PROCESSED), SmsStatus.PROCESSING),
                Arguments.of("sms in sent to client state", buildSms(SmsState.SENT_TO_CLIENT, SmsResult.SUCCESSFUL_PROCESSED), SmsStatus.SENT),

                Arguments.of("sms in new sms state with error result", buildSms(SmsState.NEW_SMS, SmsResult.ERROR), SmsStatus.FAILED),
                Arguments.of("sms in sent to sms gate state with error result", buildSms(SmsState.SENT_TO_SMS_GATE, SmsResult.ERROR), SmsStatus.FAILED),
                Arguments.of("sms in sent to client state with error result", buildSms(SmsState.SENT_TO_CLIENT, SmsResult.ERROR), SmsStatus.FAILED)
        );
    }

    private static Sms buildSms(SmsState smsState, SmsResult smsResult) {
        return new Sms(SMS_ID, SOURCE_ID, CLIENT_ID, IDEMPOTENCY_KEY, SMS_TEXT, RECEIVER_PHONE_NUMBER,
                new SmsStateDetail(smsState, smsResult, null, null),
                toString(CREATE_DATE_TIME), toString(UPDATE_DATE_TIME), true);
    }

    private static String toString(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(DATE_TIME_FORMATTER);
    }
}