package com.kivilev.service.queue.mapper;

import com.kivilev.model.Sms;
import com.kivilev.model.SmsResult;
import com.kivilev.model.SmsResultChangeMessage;
import com.kivilev.service.queue.model.SmsResultDto;
import com.kivilev.service.queue.model.SmsResultMessageDto;
import com.kivilev.service.queue.model.SmsSendMessageDto;
import org.springframework.stereotype.Component;

@Component
public class SmsMessageMapper {
    public SmsSendMessageDto toSmsSendMessageDto(Sms sms) {
        return new SmsSendMessageDto(String.valueOf(sms.getSmsId()), sms.getSourceId(), sms.getSmsText(), sms.getReceiverPhoneNumber());
    }

    public SmsResultChangeMessage toSmsResultChange(SmsResultMessageDto smsResultMessageDto) {
        return new SmsResultChangeMessage(
                smsResultMessageDto.smsId(),
                mapStatusDtoToSmsResult(smsResultMessageDto.result()),
                smsResultMessageDto.errorCode(),
                smsResultMessageDto.errorMessage()
        );
    }

    private SmsResult mapStatusDtoToSmsResult(SmsResultDto smsResultDto) {
        // TODO: бага smsStatusDto = null
        if (smsResultDto == null) {
            return SmsResult.ERROR;
        }
        switch (smsResultDto) {
            case SUCCESFULL_SEND -> {
                return SmsResult.SUCCESSFUL_PROCESSED;
            }
            case ERROR -> {
                return SmsResult.ERROR;
            }
            default -> throw new IllegalStateException("Unsupported result in sms result change message");
        }
    }
}