package com.kivilev.service.model;

import java.time.ZonedDateTime;

public class Sms {
    private Long smsId;
    private final Long clientId;
    private String sourceId;
    private final String idempotencyKey;
    private final String smsText;
    private SmsStatus smsStatus;
    private final String receiverPhoneNumber;
    private ZonedDateTime createDateTime;
    private ZonedDateTime sendReceiverDateTime;

    public Sms(Long clientId, String sourceId, String idempotencyKey, String smsText, String receiverPhoneNumber, SmsStatus smsStatus) {
        this(null, clientId, sourceId, idempotencyKey, smsText, receiverPhoneNumber, smsStatus, null, null);
    }

    public Sms(Long smsId, Long clientId, String sourceId, String idempotencyKey, String smsText, String receiverPhoneNumber,
               SmsStatus smsStatus, ZonedDateTime createDateTime, ZonedDateTime sendReceiverDateTime) {
        this.smsId = smsId;
        this.clientId = clientId;
        this.sourceId = sourceId;
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

    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public void setSendReceiverDateTime(ZonedDateTime sendReceiverDateTime) {
        this.sendReceiverDateTime = sendReceiverDateTime;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSmsStatus(SmsStatus smsStatus) {
        this.smsStatus = smsStatus;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public String toString() {
        return "Sms{" +
                "smsId=" + smsId +
                ", clientId=" + clientId +
                ", sourceId='" + sourceId + '\'' +
                ", idempotencyKey='" + idempotencyKey + '\'' +
                ", smsText='" + smsText + '\'' +
                ", smsStatus=" + smsStatus +
                ", receiverPhoneNumber='" + receiverPhoneNumber + '\'' +
                ", createDateTime=" + createDateTime +
                ", sendReceiverDateTime=" + sendReceiverDateTime +
                '}';
    }
}
