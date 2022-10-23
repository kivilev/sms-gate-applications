package com.kivilev.service.grpc.mapper;

import com.google.protobuf.Timestamp;
import com.kivilev.protobuf.generated.GetSmsStatusMessageRequest;
import com.kivilev.protobuf.generated.SendSmsRequest;
import com.kivilev.protobuf.generated.SmsMessageResponse;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsStatus;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;

@Component
public class GrpcObjectMapper {

    private final Clock clock;

    public GrpcObjectMapper(Clock clock) {
        this.clock = clock;
    }

    public SendSmsRequest toSendSmsRequest(Sms sms) {
        return SendSmsRequest.newBuilder()
                .setSource(sms.getSourceId())
                .setClientId(sms.getClientId())
                .setIdempotencyKey(sms.getIdempotencyKey())
                .setSmsText(sms.getSmsText())
                .setReceiverPhoneNumber(sms.getReceiverPhoneNumber())
                .setCreateDateTime(map(sms.getCreateDateTime()))
                .build();
    }

    public GetSmsStatusMessageRequest toSmsStatusMessageRequest(Long clientId, int limit) {
        return GetSmsStatusMessageRequest.newBuilder()
                .setClientId(clientId)
                .setLimit(limit)
                .build();
    }

    public Sms toSms(SmsMessageResponse smsResponse) {
        return new Sms(smsResponse.getSmsId(),
                smsResponse.getClientId(),
                smsResponse.getSource(),
                smsResponse.getIdempotencyKey(),
                smsResponse.getSmsText(),
                smsResponse.getReceiverPhoneNumber(),
                map(smsResponse.getStatus()),
                map(smsResponse.getCreateDateTime()),
                map(smsResponse.getSendReceiverDateTime())
        );
    }

    private SmsStatus map(com.kivilev.protobuf.generated.SmsStatus status) {
        return switch (status) {
            case PROCESSING -> SmsStatus.PROCESSING;
            case SENT -> SmsStatus.SENT;
            case FAILED, UNRECOGNIZED -> SmsStatus.FAILED;
        };
    }

    private Timestamp map(ZonedDateTime zonedDateTime) {
        return Timestamp.newBuilder()
                .setSeconds(zonedDateTime.toEpochSecond())
                .setNanos(zonedDateTime.getNano())
                .build();
    }

    private ZonedDateTime map(Timestamp timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()), clock.getZone());
    }
}