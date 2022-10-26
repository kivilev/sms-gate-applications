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
    private String providerExternalId;
    @Nonnull
    private String smsText;
    @Nonnull
    private String receiverPhoneNumber;
    private SmsStateDetail smsStateDetail;
    @Nonnull
    private boolean isSendStatusToQueue;
    @Transient
    private boolean isNew = true;

    public Sms(@Nonnull String smsId,
               String providerExternalId,
               @Nonnull String smsText,
               @Nonnull String receiverPhoneNumber,
               SmsStateDetail smsStateDetail,
               boolean isSendStatusToQueue,
               boolean isNew) {
        this.smsId = smsId;
        this.providerExternalId = providerExternalId;
        this.smsText = smsText;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.smsStateDetail = smsStateDetail;
        this.isSendStatusToQueue = isSendStatusToQueue;
        this.isNew = isNew;
    }

    @PersistenceCreator
    private Sms(@Nonnull String smsId,
                String providerExternalId,
                @Nonnull String smsText,
                @Nonnull String receiverPhoneNumber,
                SmsStateDetail smsStateDetail,
                boolean isSendStatusToQueue) {
        this(smsId, providerExternalId, smsText, receiverPhoneNumber, smsStateDetail, isSendStatusToQueue, false);
    }

    public SmsStateDetail getSmsStatusDetail() {
        return smsStateDetail;
    }

    public void setSmsStatusDetail(SmsStateDetail smsStateDetail) {
        this.smsStateDetail = smsStateDetail;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getProviderExternalId() {
        return providerExternalId;
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sms sms = (Sms) o;
        return smsId.equals(sms.smsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(smsId);
    }
}
