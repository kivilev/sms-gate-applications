package com.kivilev.model;

public class SmsResultChangeMessage {
    private String smsId;
    private final SmsResult smsResult;
    private String errorCode;
    private String errorMessage;

    public SmsResultChangeMessage(String smsId, SmsResult smsResult, String errorCode, String errorMessage) {
        this.smsId = smsId;
        this.smsResult = smsResult;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
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

    public SmsResult getSmsResult() {
        return smsResult;
    }
}
