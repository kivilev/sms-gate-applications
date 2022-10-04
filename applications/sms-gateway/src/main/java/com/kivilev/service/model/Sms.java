package com.kivilev.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import java.util.Objects;

@Table("sms")
public class Sms implements Persistable<String> {

    @Id
    @Nonnull
    private String smsId;
    private String externalId;
    @Nonnull
    private String sourceId;
    @Nonnull
    private String smsText;
    @Nonnull
    private String receiverPhoneNumber;

    private SmsStatusResultInfo smsStatusResultInfo;

    @Nonnull
    private boolean isSendStatusToQueue;

    @Transient
    private boolean isNew = true;

    public Sms(@Nonnull String smsId,
               String externalId,
               @Nonnull String sourceId,
               @Nonnull String smsText,
               @Nonnull String receiverPhoneNumber,
               SmsStatusResultInfo smsStatusResultInfo,
               boolean isSendStatusToQueue,
               boolean isNew) {
        this.smsId = smsId;
        this.externalId = externalId;
        this.sourceId = sourceId;
        this.smsText = smsText;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.smsStatusResultInfo = smsStatusResultInfo;
        this.isSendStatusToQueue = isSendStatusToQueue;
        this.isNew = isNew;
    }

    @PersistenceCreator
    private Sms(@Nonnull String smsId,
                String externalId,
                @Nonnull String sourceId,
                @Nonnull String smsText,
                @Nonnull String receiverPhoneNumber,
                SmsStatusResultInfo smsStatusResultInfo,
                boolean isSendStatusToQueue) {
        this(smsId, externalId, sourceId, smsText, receiverPhoneNumber, smsStatusResultInfo, isSendStatusToQueue, false);
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

    @Override
    public String getId() {
        return smsId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sms sms = (Sms) o;
        return smsId.equals(sms.smsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(smsId);
    }
}
