package com.kivilev.model;

import org.springframework.data.relational.core.mapping.Table;

@Table("sms_state_detail")
public class SmsStateDetail {
    private SmsState smsState;
    private SmsResult smsResult;
    private String errorCode;
    private String errorMessage;

    public SmsStateDetail(SmsState smsState, SmsResult smsResult, String errorCode, String errorMessage) {
        this.smsState = smsState;
        this.smsResult = smsResult;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public SmsState getSmsStatus() {
        return smsState;
    }

    public void setSmsStatus(SmsState smsState) {
        this.smsState = smsState;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public SmsState getSmsState() {
        return smsState;
    }

    public void setSmsState(SmsState smsState) {
        this.smsState = smsState;
    }

    public SmsResult getSmsResult() {
        return smsResult;
    }

    public void setSmsResult(SmsResult smsResult) {
        this.smsResult = smsResult;
    }
}
