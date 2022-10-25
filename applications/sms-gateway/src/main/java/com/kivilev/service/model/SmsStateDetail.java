package com.kivilev.service.model;

import org.springframework.data.relational.core.mapping.Table;

@Table("sms_state_detail")
public class SmsStateDetail {
    private final SmsState smsState;
    private final SmsResult smsResult;
    private final String errorCode;
    private final String errorMessage;

    public SmsStateDetail(SmsState smsState, SmsResult smsResult, String errorCode, String errorMessage) {
        this.smsState = smsState;
        this.smsResult = smsResult;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public SmsState getSmsStatus() {
        return smsState;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public SmsResult getSmsResult() {
        return smsResult;
    }
}
