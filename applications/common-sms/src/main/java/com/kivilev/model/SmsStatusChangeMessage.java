package com.kivilev.model;

public class SmsStatusChangeMessage {
    private String smsId;
    private SmsState smsState;
    private String errorCode;
    private String errorMessage;

    public SmsStatusChangeMessage(String smsId, SmsState smsState, String errorCode, String errorMessage) {
        this.smsId = smsId;
        this.smsState = smsState;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public SmsState getSmsState() {
        return smsState;
    }

    public void setSmsState(SmsState smsState) {
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
}
