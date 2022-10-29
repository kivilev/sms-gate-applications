package com.kivilev.service.grpc.mapper;

import com.google.protobuf.Timestamp;
import com.kivilev.model.Sms;
import com.kivilev.model.SmsResult;
import com.kivilev.model.SmsState;
import com.kivilev.model.SmsStateDetail;
import com.kivilev.protobuf.generated.SendSmsRequest;
import com.kivilev.protobuf.generated.SmsMessageResponse;
import com.kivilev.protobuf.generated.SmsStatus;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.kivilev.protobuf.generated.SmsStatus.FAILED;

@Component
public class GrpcObjectMapper {

    private final Clock clock;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public GrpcObjectMapper(Clock clock) {
        this.clock = clock;
    }

    public Sms toSms(SendSmsRequest sendSmsRequest) {
        return new Sms(null,
                sendSmsRequest.getSource(),
                sendSmsRequest.getClientId(),
                sendSmsRequest.getIdempotencyKey(),
                sendSmsRequest.getSmsText(),
                sendSmsRequest.getReceiverPhoneNumber(),
                new SmsStateDetail(SmsState.NEW_SMS, SmsResult.SUCCESSFUL_PROCESSED, null, null),
                map(sendSmsRequest.getCreateDateTime()),
                map(ZonedDateTime.now(clock)),
                true
        );
    }

    public SmsMessageResponse toSmsMessageResponse(Sms sms) {
        return SmsMessageResponse
                .newBuilder()
                .setSmsId(sms.getSmsId())
                .setClientId(sms.getClientId())
                .setSource(sms.getSourceId())
                .setIdempotencyKey(sms.getSourceIdempotencyKey())
                .setSmsText(sms.getSmsText())
                .setReceiverPhoneNumber(sms.getReceiverPhoneNumber())
                .setCreateDateTime(map(sms.getCreateDateTime()))
                .setSendReceiverDateTime(map(sms.getChangeStatusDateTime()))
                .setStatus(mapStatus(sms.getSmsStatusInfo().getSmsStatus(), sms.getSmsStatusInfo().getSmsResult()))
                .build();
    }

    private SmsStatus mapStatus(SmsState state, SmsResult result) {
        if (result == SmsResult.ERROR) {
            return FAILED;
        }

        return switch (state) {
            case NEW_SMS, SENT_TO_SMS_GATE -> SmsStatus.PROCESSING;
            case SENT_TO_CLIENT -> SmsStatus.SENT;
        };
    }

    private Timestamp map(String dtime) {
        var zonedDateTime = ZonedDateTime.parse(dtime, formatter);
        return Timestamp.newBuilder()
                .setSeconds(zonedDateTime.toEpochSecond())
                .setNanos(zonedDateTime.getNano())
                .build();
    }

    private String map(Timestamp timestamp) {
        var dtime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()), clock.getZone());
        return map(dtime);
    }


    private String map(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(formatter);
    }


/*    private Timestamp map(Instant zonedDateTime) {
        return Timestamp.newBuilder()
                .setSeconds(zonedDateTime.toEpochSecond())
                .setNanos(zonedDateTime.getNano())
                .build();
    }

    private Instant map(Timestamp timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()), clock.getZone());
    }*/

}