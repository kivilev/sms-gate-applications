package com.kivilev.service.queue.mapper;

import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.model.SmsStateDetail;
import com.kivilev.service.queue.model.SmsResultDto;
import com.kivilev.service.queue.model.SmsResultMessageDto;
import com.kivilev.service.queue.model.SmsSendMessageDto;
import org.springframework.stereotype.Component;

@Component
public class SmsMessageMapper {
    public SmsResultMessageDto toSmsStatusMessageDto(Sms sms) {
        return new SmsResultMessageDto(sms.getSmsId(),
                toSmsStatusDto(sms.getSmsStatusDetail().getSmsResult()),
                sms.getSmsStatusDetail().getErrorCode(),
                sms.getSmsStatusDetail().getErrorMessage()
        );
    }

    public Sms toSms(SmsSendMessageDto smsSendMessageDto) {
        var smsStatusInfo = new SmsStateDetail(SmsState.NEW_FROM_QUEUE, SmsResult.SUCCESSFUL_PROCESSED, null, null);
        return new Sms(smsSendMessageDto.smsId(),
                null,
                smsSendMessageDto.smsText(),
                smsSendMessageDto.receiverPhoneNumber(),
                smsStatusInfo,
                false,
                true);
    }

    private SmsResultDto toSmsStatusDto(SmsResult smsResult) {
        return smsResult == SmsResult.ERROR ? SmsResultDto.ERROR : SmsResultDto.SUCCESFULL_SEND;
    }
}
