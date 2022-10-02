package com.kivilev.model;

public class SmsStatusInfo {
    private SmsState smsState;
    private String errorCode;
    private String errorMessage;

    public SmsStatusInfo(SmsState smsState, String errorCode, String errorMessage) {
        this.smsState = smsState;
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

}
