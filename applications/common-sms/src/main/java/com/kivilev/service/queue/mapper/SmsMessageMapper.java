package com.kivilev.service.queue.mapper;

import com.kivilev.model.Sms;
import com.kivilev.model.SmsState;
import com.kivilev.model.SmsStatusChangeMessage;
import com.kivilev.service.queue.model.SmsSendMessageDto;
import com.kivilev.service.queue.model.SmsStatusChangeMessageDto;
import com.kivilev.service.queue.model.SmsStatusDto;
import org.springframework.stereotype.Component;

@Component
public class SmsMessageMapper {
    public SmsSendMessageDto toSmsSendMessageDto(Sms sms) {
        return new SmsSendMessageDto(sms.getSmsId(), sms.getSourceId(), sms.getSmsText(), sms.getReceiverPhoneNumber());
    }

    public SmsStatusChangeMessage toSmsStatusChange(SmsStatusChangeMessageDto smsStatusChangeMessageDto) {
        return new SmsStatusChangeMessage(smsStatusChangeMessageDto.smsId(),
                mapStatusDtoToSmsStatus(smsStatusChangeMessageDto.status()),
                smsStatusChangeMessageDto.errorCode(),
                smsStatusChangeMessageDto.errorMessage());
    }

    private SmsState mapStatusDtoToSmsStatus(SmsStatusDto smsStatusDto) {
        // TODO: бага smsStatusDto = null
        if (smsStatusDto == null) {
            return SmsState.SENT_TO_CLIENT;
        }
        switch (smsStatusDto) {
            case SUCCESFULL_SEND -> {
                return SmsState.SENT_TO_CLIENT;
            }
            case ERROR -> {
                return SmsState.ERROR;
            }
            default -> throw new IllegalStateException("Unsupported status in sms status change message");
        }
    }
}