package com.kivilev.service.queue.mapper;

import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.model.SmsStatusResultInfo;
import com.kivilev.service.queue.model.SmsSendMessageDto;
import com.kivilev.service.queue.model.SmsStatusChangeMessageDto;
import com.kivilev.service.queue.model.SmsStatusDto;
import org.springframework.stereotype.Component;

@Component
public class SmsMessageMapper {
    public SmsStatusChangeMessageDto toSmsStatusMessageDto(Sms sms) {
        return new SmsStatusChangeMessageDto(sms.getSmsId(),
                sms.getSourceId(),
                toSmsStatusDto(sms.getSmsStatusInfo().getSmsResult()),
                sms.getSmsStatusInfo().getErrorCode(),
                sms.getSmsStatusInfo().getErrorMessage()
        );
    }

    public Sms toSms(SmsSendMessageDto smsSendMessageDto) {
        var smsStatusInfo = new SmsStatusResultInfo(SmsState.NEW_FROM_QUEUE, SmsResult.SUCCESSFUL_PROCESSED, null, null);
        return new Sms(smsSendMessageDto.smsId(),
                null,
                smsSendMessageDto.sourceId(),
                smsSendMessageDto.smsText(),
                smsSendMessageDto.receiverPhoneNumber(),
                smsStatusInfo,
                false,
                true);
    }

    private SmsStatusDto toSmsStatusDto(SmsResult smsResult) {
        return smsResult == SmsResult.ERROR ? SmsStatusDto.ERROR : SmsStatusDto.SUCCESFULL_SEND;
    }
}
