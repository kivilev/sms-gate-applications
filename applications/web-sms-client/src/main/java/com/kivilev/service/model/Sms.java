package com.kivilev.service.model;

import java.time.ZonedDateTime;

public class Sms {
    private Long smsId;
    private Long clientId;
    private String idempotencyKey;
    private String smsText;
    private SmsStatus smsStatus;
    private String receiverPhoneNumber;
    private ZonedDateTime createDateTime;
    private ZonedDateTime sendReceiverDateTime;

    public Sms(Long clientId, String idempotencyKey, String smsText, String receiverPhoneNumber, SmsStatus smsStatus) {
        this(null, clientId, idempotencyKey, smsText, smsStatus, receiverPhoneNumber, null, null);
    }

    public Sms(Long smsId, Long clientId, String idempotencyKey, String smsText, SmsStatus smsStatus, String receiverPhoneNumber, ZonedDateTime createDateTime, ZonedDateTime sendReceiverDateTime) {
        this.smsId = smsId;
        this.clientId = clientId;
        this.idempotencyKey = idempotencyKey;
        this.smsText = smsText;
        this.smsStatus = smsStatus;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.createDateTime = createDateTime;
        this.sendReceiverDateTime = sendReceiverDateTime;
    }

    public Long getSmsId() {
        return smsId;
    }

    public Long getClientId() {
        return clientId;
    }

    public String getSmsText() {
        return smsText;
    }

    public SmsStatus getSmsStatus() {
        return smsStatus;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    public ZonedDateTime getSendReceiverDateTime() {
        return sendReceiverDateTime;
    }

    public void setSmsId(Long smsId) {
        this.smsId = smsId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public void setSmsStatus(SmsStatus smsStatus) {
        this.smsStatus = smsStatus;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public void setSendReceiverDateTime(ZonedDateTime sendReceiverDateTime) {
        this.sendReceiverDateTime = sendReceiverDateTime;
    }
}
