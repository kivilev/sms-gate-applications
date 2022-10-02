package com.kivilev.model;

public class Sms {
    private String smsId;
    private String sourceId;
    private String sourceIdempotencyKey;
    private String smsText;
    private String receiverPhoneNumber;
    private SmsStatusInfo smsStatusInfo;

    public Sms(String smsId, String sourceId, String sourceIdempotencyKey, String smsText, String receiverPhoneNumber, SmsStatusInfo smsStatusInfo) {
        this.smsId = smsId;
        this.sourceId = sourceId;
        this.sourceIdempotencyKey = sourceIdempotencyKey;
        this.smsText = smsText;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.smsStatusInfo = smsStatusInfo;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public SmsStatusInfo getSmsStatusInfo() {
        return smsStatusInfo;
    }

    public void setSmsStatusInfo(SmsStatusInfo smsStatusInfo) {
        this.smsStatusInfo = smsStatusInfo;
    }

    public String getSourceIdempotencyKey() {
        return sourceIdempotencyKey;
    }

    public void setSourceIdempotencyKey(String sourceIdempotencyKey) {
        this.sourceIdempotencyKey = sourceIdempotencyKey;
    }
}
