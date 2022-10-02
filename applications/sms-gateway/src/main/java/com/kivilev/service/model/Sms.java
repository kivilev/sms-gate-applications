package com.kivilev.service.model;

public class Sms {
    private String smsId;
    private String externalId;
    private String sourceId;
    private String smsText;
    private String receiverPhoneNumber;
    private SmsStatusResultInfo smsStatusResultInfo;
    private boolean isSendStatusToQueue;

    public Sms(String smsId, String externalId, String sourceId, String smsText, String receiverPhoneNumber, SmsStatusResultInfo smsStatusResultInfo, boolean isSendStatusToQueue) {
        this.smsId = smsId;
        this.externalId = externalId;
        this.sourceId = sourceId;
        this.smsText = smsText;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.smsStatusResultInfo = smsStatusResultInfo;
        this.isSendStatusToQueue = isSendStatusToQueue;
    }

    public SmsStatusResultInfo getSmsStatusInfo() {
        return smsStatusResultInfo;
    }

    public void setSmsStatusInfo(SmsStatusResultInfo smsStatusResultInfo) {
        this.smsStatusResultInfo = smsStatusResultInfo;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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

    public boolean isSendStatusToQueue() {
        return isSendStatusToQueue;
    }

    public void setSendStatusToQueue(boolean sendStatusToQueue) {
        isSendStatusToQueue = sendStatusToQueue;
    }

}
