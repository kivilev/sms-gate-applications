package com.kivilev.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import java.util.Objects;

@Table("sms")
public class Sms implements Persistable<Long> {
    @Id
    @Nonnull
    private Long smsId;
    @Nonnull
    private String sourceId;
    @Nonnull
    private String sourceIdempotencyKey;
    @Nonnull
    private String smsText;
    @Nonnull
    private String receiverPhoneNumber;
    private SmsStateDetail smsStateDetail;

    @Transient
    private boolean isNew = true;

    public Sms(Long smsId, String sourceId, String sourceIdempotencyKey, String smsText, String receiverPhoneNumber, SmsStateDetail smsStateDetail, boolean isNew) {
        this.smsId = smsId;
        this.sourceId = sourceId;
        this.sourceIdempotencyKey = sourceIdempotencyKey;
        this.smsText = smsText;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.smsStateDetail = smsStateDetail;
        this.isNew = isNew;
    }

    @PersistenceCreator
    public Sms(Long smsId, String sourceId, String sourceIdempotencyKey, String smsText, String receiverPhoneNumber, SmsStateDetail smsStateDetail) {
        this(smsId, sourceId, sourceIdempotencyKey, smsText, receiverPhoneNumber, smsStateDetail, false);
    }

    public Long getSmsId() {
        return smsId;
    }

    public void setSmsId(Long smsId) {
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

    public SmsStateDetail getSmsStatusInfo() {
        return smsStateDetail;
    }

    public void setSmsStatusInfo(SmsStateDetail smsStateDetail) {
        this.smsStateDetail = smsStateDetail;
    }

    public String getSourceIdempotencyKey() {
        return sourceIdempotencyKey;
    }

    public void setSourceIdempotencyKey(String sourceIdempotencyKey) {
        this.sourceIdempotencyKey = sourceIdempotencyKey;
    }

    @Override
    public Long getId() {
        return smsId;
    }

    @Override
    public boolean isNew() {
        return isNew;
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
