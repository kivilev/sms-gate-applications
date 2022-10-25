package com.kivilev.controller.api.v1.mapper;

import com.kivilev.controller.api.v1.model.SendNewSmsRequestDto;
import com.kivilev.controller.api.v1.model.SendNewSmsResponseDto;
import com.kivilev.controller.api.v1.model.SmsStatusDto;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsStatus;
import org.springframework.stereotype.Component;

@Component
public class SmsRequestResponseMapper {

    public Sms toSms(SendNewSmsRequestDto sendNewSmsRequestDto) {
        return new Sms(sendNewSmsRequestDto.clientId(),
                null,
                sendNewSmsRequestDto.idempotencyKey(),
                sendNewSmsRequestDto.smsText(),
                sendNewSmsRequestDto.receiverPhoneNumber(),
                SmsStatus.PROCESSING);
    }

    public SendNewSmsResponseDto toSendNewSmsResponseDto(Sms sms) {
        return new SendNewSmsResponseDto(sms.getSmsId(), toSmsStatusDto(sms.getSmsStatus()), sms.getCreateDateTime(), sms.getSendReceiverDateTime());
    }

    private SmsStatusDto toSmsStatusDto(SmsStatus smsStatus) {
        return switch (smsStatus) {
            case FAILED -> SmsStatusDto.FAILED;
            case SENT -> SmsStatusDto.SENT;
            case PROCESSING -> SmsStatusDto.WAITING;
        };
    }
}
